package Service;

import Entity.Order;
import Entity.OrderItem;
import Entity.StockKeepingUnit;
import Enums.OrderStatus;
import Enums.ProductStatus;
import Repository.OrderRepository;
import Repository.SkuRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final SkuRepository skuRepo;
    private final InventoryService inventoryService;

    private final ConcurrentHashMap<String, ReentrantLock> orderLocks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public OrderServiceImpl(OrderRepository orderRepo,
                            SkuRepository skuRepo,
                            InventoryService inventoryService) {
        this.orderRepo = orderRepo;
        this.skuRepo = skuRepo;
        this.inventoryService = inventoryService;
    }

    private ReentrantLock lockForOrder(String orderId) {
        return orderLocks.computeIfAbsent(orderId, k -> new ReentrantLock());
    }

    @Override
    public Order createOrder(String customerId, List<OrderItem> items) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId cannot be null/blank");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items cannot be null/empty");
        }

        Map<String, Integer> qtyBySku = new HashMap<>();
        for (OrderItem item : items) {
            if (item.getSkuId() == null || item.getSkuId().isBlank()) {
                throw new IllegalArgumentException("skuId cannot be null/blank");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("quantity must be > 0");
            }
            qtyBySku.merge(item.getSkuId(), item.getQuantity(), Integer::sum);
        }

        List<OrderItem> normalizedItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> e : qtyBySku.entrySet()) {
            String skuId = e.getKey();
            int qty = e.getValue();

            StockKeepingUnit sku = skuRepo.findById(skuId)
                    .orElseThrow(() -> new RuntimeException("SKU not found: " + skuId));

            if (sku.getProductStatus() == ProductStatus.INACTIVE) {
                throw new RuntimeException("SKU inactive: " + skuId);
            }

            BigDecimal unitPrice = sku.getPrice();
            normalizedItems.add(new OrderItem(skuId, qty, unitPrice));
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(qty)));
        }

        inventoryService.reserveItems(normalizedItems);

        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, customerId, OrderStatus.RESERVED, normalizedItems, total);
        orderRepo.save(order);
        return order;
    }

    @Override
    public Order getOrder(String orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    @Override
    public void confirmOrder(String orderId) {
        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
                return;
            }
            if (order.getOrderStatus() == OrderStatus.CANCELLED ||
                    order.getOrderStatus() == OrderStatus.EXPIRED) {
                throw new RuntimeException("Order cannot be confirmed in state: " + order.getOrderStatus());
            }

            if (order.isExpired()) {
                expireOrderInternal(order);
                throw new RuntimeException("Order already expired");
            }

            inventoryService.confirmReservedItems(order.getOrderItemList());
            order.transitionTo(OrderStatus.CONFIRMED);
            orderRepo.save(order);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancelOrder(String orderId) {
        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            if (order.getOrderStatus() == OrderStatus.CANCELLED ||
                    order.getOrderStatus() == OrderStatus.EXPIRED) {
                return;
            }
            if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
                throw new RuntimeException("Confirmed order cannot be cancelled");
            }

            inventoryService.releaseItems(order.getOrderItemList());
            order.transitionTo(OrderStatus.CANCELLED);
            orderRepo.save(order);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void startExpiryScheduler() {
        scheduler.scheduleAtFixedRate(this::cleanupExpiredOrders, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void shutdownScheduler() {
        scheduler.shutdown();
    }

    private void cleanupExpiredOrders() {
        Collection<Order> allOrders = orderRepo.findAll();
        Instant now = Instant.now();

        for (Order order : allOrders) {
            if (order.getOrderStatus() == OrderStatus.RESERVED &&
                    !now.isBefore(order.getExpiresAt())) {
                ReentrantLock lock = lockForOrder(order.getOrderId());
                lock.lock();
                try {
                    Order fresh = orderRepo.findById(order.getOrderId()).orElse(null);
                    if (fresh == null) {
                        continue;
                    }
                    if (fresh.getOrderStatus() == OrderStatus.RESERVED && fresh.isExpired()) {
                        expireOrderInternal(fresh);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private void expireOrderInternal(Order order) {
        if (order.getOrderStatus() != OrderStatus.RESERVED) {
            return;
        }
        if (!order.isExpired()) {
            return;
        }

        inventoryService.releaseItems(order.getOrderItemList());
        order.transitionTo(OrderStatus.EXPIRED);
        orderRepo.save(order);
    }
}
