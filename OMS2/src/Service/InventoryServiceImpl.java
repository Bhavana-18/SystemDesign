package Service;

import Entity.InventoryRecord;
import Entity.OrderItem;
import Repository.InventoryRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ConcurrentHashMap<String, ReentrantLock> skuLocks;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
        this.skuLocks = new ConcurrentHashMap<>();
    }

    @Override
    public void addStock(String skuId, int qty) {
        if (skuId == null || skuId.isBlank()) {
            throw new IllegalArgumentException("skuId cannot be null/blank");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("qty must be > 0");
        }

        ReentrantLock lock = skuLocks.computeIfAbsent(skuId, k -> new ReentrantLock());
        lock.lock();
        try {
            InventoryRecord record = inventoryRepository.findById(skuId)
                    .orElseGet(() -> new InventoryRecord(skuId, 0));
            record.addStock(qty);
            inventoryRepository.save(record);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void reserveItems(List<OrderItem> orderItemList) {
        if (orderItemList == null || orderItemList.isEmpty()) {
            throw new IllegalArgumentException("orderItemList cannot be null/empty");
        }

        List<String> skuIds = extractSortedSkuIds(orderItemList);
        List<ReentrantLock> locks = lockAll(skuIds);

        try {
            for (OrderItem item : orderItemList) {
                InventoryRecord record = inventoryRepository.findById(item.getSkuId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found for skuId: " + item.getSkuId()));
                if (record.getAvailableQty() < item.getQuantity()) {
                    throw new RuntimeException("Insufficient inventory for skuId: " + item.getSkuId());
                }
            }

            for (OrderItem item : orderItemList) {
                InventoryRecord record = inventoryRepository.findById(item.getSkuId()).get();
                record.reserve(item.getQuantity());
                inventoryRepository.save(record);
            }
        } finally {
            unlockAllReverse(locks);
        }
    }

    @Override
    public void confirmReservedItems(List<OrderItem> orderItemList) {
        if (orderItemList == null || orderItemList.isEmpty()) {
            throw new IllegalArgumentException("orderItemList cannot be null/empty");
        }

        List<String> skuIds = extractSortedSkuIds(orderItemList);
        List<ReentrantLock> locks = lockAll(skuIds);

        try {
            for (OrderItem item : orderItemList) {
                InventoryRecord record = inventoryRepository.findById(item.getSkuId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found for skuId: " + item.getSkuId()));
                record.confirmReserved(item.getQuantity());
                inventoryRepository.save(record);
            }
        } finally {
            unlockAllReverse(locks);
        }
    }

    @Override
    public void releaseItems(List<OrderItem> orderItemList) {
        if (orderItemList == null || orderItemList.isEmpty()) {
            throw new IllegalArgumentException("orderItemList cannot be null/empty");
        }

        List<String> skuIds = extractSortedSkuIds(orderItemList);
        List<ReentrantLock> locks = lockAll(skuIds);

        try {
            for (OrderItem item : orderItemList) {
                InventoryRecord record = inventoryRepository.findById(item.getSkuId())
                        .orElseThrow(() -> new RuntimeException("Inventory not found for skuId: " + item.getSkuId()));
                record.releaseReserved(item.getQuantity());
                inventoryRepository.save(record);
            }
        } finally {
            unlockAllReverse(locks);
        }
    }

    @Override
    public int getAvailableStock(String skuId) {
        if (skuId == null || skuId.isBlank()) {
            throw new IllegalArgumentException("skuId cannot be null/blank");
        }
        return inventoryRepository.findById(skuId)
                .map(InventoryRecord::getAvailableQty)
                .orElse(0);
    }

    private List<String> extractSortedSkuIds(List<OrderItem> orderItemList) {
        List<String> skuIds = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            if (item.getSkuId() == null || item.getSkuId().isBlank()) {
                throw new IllegalArgumentException("skuId cannot be null/blank");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("qty must be > 0");
            }
            skuIds.add(item.getSkuId());
        }
        Collections.sort(skuIds);
        return skuIds;
    }

    private List<ReentrantLock> lockAll(List<String> skuIds) {
        List<ReentrantLock> locks = new ArrayList<>(skuIds.size());
        for (String skuId : skuIds) {
            ReentrantLock lock = skuLocks.computeIfAbsent(skuId, k -> new ReentrantLock());
            locks.add(lock);
        }
        for (ReentrantLock lock : locks) {
            lock.lock();
        }
        return locks;
    }

    private void unlockAllReverse(List<ReentrantLock> locks) {
        for (int i = locks.size() - 1; i >= 0; i--) {
            locks.get(i).unlock();
        }
    }
}