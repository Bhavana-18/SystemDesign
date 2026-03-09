package Service;

import Entity.InventoryRecord;
import Entity.OrderItem;
import Entity.StockKeepingUnit;
import Repository.InventoryRepository;
import Repository.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ConcurrentHashMap<String, ReentrantLock> skuLocks;

    public InventoryServiceImpl(InventoryRepository inventoryRepository){
        this.inventoryRepository =  inventoryRepository;
        skuLocks = new ConcurrentHashMap<>();
    }


    @Override
    public void addStock(String skuId, int qty){
        if(skuId == null || skuId.isBlank()){
            throw  new IllegalArgumentException("skuId cannot be null/blank");
        }
        if(qty <= 0){
            throw  new IllegalArgumentException("qty must be > 0");
        }
        //add catalog validation
        skuLocks.computeIfAbsent(skuId, k ->new ReentrantLock());
        ReentrantLock lock = skuLocks.get(skuId);
        lock.lock();
        try{
          InventoryRecord record = inventoryRepository.findById(skuId).orElseGet(() -> new InventoryRecord(skuId, 0));
           record.increase(qty);
           inventoryRepository.save(record);

        }finally {
            lock.unlock();
        }
    }

    @Override
    public void reserveItems(List<OrderItem> orderItemList){
        if(orderItemList == null || orderItemList.isEmpty()){
            throw new IllegalArgumentException("orderItemList cannot be null or Empty");
        }

        Map<String, Integer> required = mergeBySku(orderItemList);
        List<String > skuIds = new ArrayList<>(required.keySet());
        Collections.sort(skuIds);
        List<ReentrantLock> locks = new ArrayList<>(skuIds.size());
        for(String skuId : skuIds){
            ReentrantLock lock = skuLocks.computeIfAbsent(skuId, k-> new ReentrantLock());
            locks.add(lock);
        }
        for(ReentrantLock lock: locks){
            lock.lock();
        }

        try {
            for (String skuId : skuIds) {
                int qtyNeeded = required.get(skuId);
                InventoryRecord record = inventoryRepository.findById(skuId).orElseGet(() -> new InventoryRecord(skuId, 0));
                record.decrease(qtyNeeded);
                inventoryRepository.save(record);


            }
        }
            finally {
            for (int i = locks.size() - 1; i >= 0; i--) {
                locks.get(i).unlock();
            }
        }
    }

    @Override
    public void releaseItems(List<OrderItem> orderItemList){
        Map<String, Integer> toRelease = mergeBySku(orderItemList);
        List<String> skuIds = new ArrayList<>(toRelease.keySet());
        Collections.sort(skuIds);

        List<ReentrantLock> locks = new ArrayList<>();
        for (String skuId : skuIds) {
            locks.add(skuLocks.computeIfAbsent(skuId, k -> new ReentrantLock()));
        }

        for (ReentrantLock lock : locks) lock.lock();
        try {
            for (String skuId : skuIds) {
                InventoryRecord record = inventoryRepository.findById(skuId)
                        .orElseGet(() -> new InventoryRecord(skuId, 0)); // release can create record if you allow
                record.increase(toRelease.get(skuId));
                inventoryRepository.save(record);
            }
        } finally {
            for (int i = locks.size() - 1; i >= 0; i--) locks.get(i).unlock();
        }

    }
    private Map<String, Integer> mergeBySku(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items cannot be null/empty");
        }
        return items.stream().collect(Collectors.toMap(
                item -> {
                    if (item.getSkuId() == null || item.getSkuId().isBlank())
                        throw new IllegalArgumentException("skuId cannot be null/blank");
                    if (item.getQuantity() <= 0)
                        throw new IllegalArgumentException("qty must be > 0");
                    return item.getSkuId();
                },
                OrderItem::getQuantity,
                Integer::sum
        ));
    }



}
