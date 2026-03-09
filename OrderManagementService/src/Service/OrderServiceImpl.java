package Service;

import Entity.*;


import Enums.OrderStatus;
import Enums.PaymentMode;
import Enums.ProductStatus;


import Repository.OrderRepository;
import Repository.SkuRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final SkuRepository skuRepo;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShipmentService shipmentService;

    // Per-order locks (aggregate root lock)
    private final ConcurrentHashMap<String, ReentrantLock> orderLocks = new ConcurrentHashMap<>();

    public OrderServiceImpl(OrderRepository orderRepo,
                            SkuRepository skuRepo,
                            InventoryService inventoryService,
                            PaymentService paymentService,
                            ShipmentService shipmentService) {
        this.orderRepo = Objects.requireNonNull(orderRepo);
        this.skuRepo = Objects.requireNonNull(skuRepo);
        this.inventoryService = Objects.requireNonNull(inventoryService);
        this.paymentService = Objects.requireNonNull(paymentService);
        this.shipmentService = Objects.requireNonNull(shipmentService);
    }

    private ReentrantLock lockForOrder(String orderId) {
        return orderLocks.computeIfAbsent(orderId, k -> new ReentrantLock());
    }

    // -----------------------------
    // Order Creation / Query
    // -----------------------------

    @Override
    public Order createOrder(String customerId, List<OrderItem> items) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId cannot be null/blank");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items cannot be null/empty");
        }

        // 1) Validate SKUs exist & ACTIVE, compute total, snapshot unitPriceAtPurchase, and merge duplicates.
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

            BigDecimal unitPrice = sku.getPrice(); // assume BigDecimal
            normalizedItems.add(new OrderItem(skuId, qty, unitPrice));
            total = total.add(unitPrice.multiply(BigDecimal.valueOf(qty)));
        }

        // 2) Reserve inventory atomically (skuLocks live inside InventoryService)
        // NOTE: createOrder does NOT take orderLock because order doesn't exist yet.
        inventoryService.reserveItems(normalizedItems);

        // 3) Create and persist order
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, customerId, OrderStatus.RESERVED, normalizedItems, total);
        orderRepo.save(order);
        return order;
    }

    @Override
    public Order getOrder(String orderId) {
        if (orderId == null || orderId.isBlank()) throw new IllegalArgumentException("orderId cannot be null/blank");
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    @Override
    public List<Order> getOrdersByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) throw new IllegalArgumentException("customerId cannot be null/blank");
        // If your repo supports querying, use it; else do a simple scan (fine for in-memory).
        List<Order> result = new ArrayList<>();
        for (Order o : orderRepo.findAll()) {
            if (customerId.equals(o.getCustomerId())) result.add(o);
        }
        return result;
    }

    // -----------------------------
    // Payment Lifecycle
    // -----------------------------

    @Override
    public Payment initiatePayment(String orderId, PaymentMode mode) {
        if (mode == null) throw new IllegalArgumentException("payment mode cannot be null");
        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            // Idempotency: if already PAYMENT_PENDING and paymentId exists, you can return existing payment.
            if (order.getOrderStatus() == OrderStatus.PAYMENT_PENDING && order.getPaymentId() != null) {
                return paymentService.get(order.getPaymentId());
            }

            requireStatus(order, OrderStatus.RESERVED);

            Payment payment = paymentService.initiate(orderId, mode);
            order.setPaymentId(payment.getPaymentId());
            order.setOrderStatus(OrderStatus.PAYMENT_PENDING);

            orderRepo.save(order);
            return payment;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void confirmPayment(String orderId, String paymentId, String externalRef) {
        if (paymentId == null || paymentId.isBlank()) throw new IllegalArgumentException("paymentId cannot be null/blank");
        if (externalRef == null || externalRef.isBlank()) throw new IllegalArgumentException("externalRef cannot be null/blank");

        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            // Must match the order's paymentId (prevents confirming wrong payment)
            if (order.getPaymentId() == null || !order.getPaymentId().equals(paymentId)) {
                throw new RuntimeException("PaymentId mismatch for order. orderId=" + orderId);
            }

            // Idempotency: already paid => no-op
            if (order.getOrderStatus() == OrderStatus.PAID) return;

            requireStatus(order, OrderStatus.PAYMENT_PENDING);

            paymentService.markSuccess(paymentId, externalRef);
            order.setOrderStatus(OrderStatus.PAID);

            orderRepo.save(order);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void failPayment(String orderId, String paymentId, String reason) {
        if (paymentId == null || paymentId.isBlank()) throw new IllegalArgumentException("paymentId cannot be null/blank");
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("reason cannot be null/blank");

        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            if (order.getPaymentId() == null || !order.getPaymentId().equals(paymentId)) {
                throw new RuntimeException("PaymentId mismatch for order. orderId=" + orderId);
            }

            // If already paid, cannot fail
            if (order.getOrderStatus() == OrderStatus.PAID) {
                throw new RuntimeException("Order already PAID; cannot fail payment. orderId=" + orderId);
            }

            // Idempotency: if already cancelled/refund, ignore or throw based on your policy
            if (order.getOrderStatus() == OrderStatus.CANCELLED) return;

            requireStatus(order, OrderStatus.PAYMENT_PENDING);

            paymentService.markFailed(paymentId, reason);

            // Policy choice: cancel on payment failure + release inventory
            order.setOrderStatus(OrderStatus.CANCELLED);
            // ORDER -> SKU lock acquisition (InventoryService locks internally). Safe if you never do SKU->ORDER anywhere.
            inventoryService.releaseItems(order.getOrderItemList());

            orderRepo.save(order);
        } finally {
            lock.unlock();
        }
    }

    // -----------------------------
    // Shipment Lifecycle
    // -----------------------------

    @Override
    public Shipment createShipment(String orderId) {
        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            // Idempotency: if already created, return existing
            if (order.getOrderStatus() == OrderStatus.FULFILLMENT_CREATED && order.getShipmentId() != null) {
                return shipmentService.get(order.getShipmentId());
            }

            requireStatus(order, OrderStatus.PAID);

            Shipment shipment = shipmentService.create(orderId);
            order.setShipmentId(shipment.getShipmentId());
            order.setOrderStatus(OrderStatus.FULFILLMENT_CREATED);

            orderRepo.save(order);
            return shipment;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void dispatch(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) throw new IllegalArgumentException("shipmentId cannot be null/blank");

        Shipment shipment = shipmentService.get(shipmentId);
        String orderId = shipment.getOrderId();

        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            if (order.getShipmentId() == null || !order.getShipmentId().equals(shipmentId)) {
                throw new RuntimeException("ShipmentId mismatch for order. orderId=" + orderId);
            }

            // Idempotency
            if (order.getOrderStatus() == OrderStatus.DISPATCHED) return;
            if (order.getOrderStatus() == OrderStatus.DELIVERED) {
                throw new RuntimeException("Order already DELIVERED; cannot dispatch. orderId=" + orderId);
            }

            requireStatus(order, OrderStatus.FULFILLMENT_CREATED);

            shipmentService.markDispatched(shipmentId);
            order.setOrderStatus(OrderStatus.DISPATCHED);

            orderRepo.save(order);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deliver(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) throw new IllegalArgumentException("shipmentId cannot be null/blank");

        Shipment shipment = shipmentService.get(shipmentId);
        String orderId = shipment.getOrderId();

        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            if (order.getShipmentId() == null || !order.getShipmentId().equals(shipmentId)) {
                throw new RuntimeException("ShipmentId mismatch for order. orderId=" + orderId);
            }

            // Idempotency
            if (order.getOrderStatus() == OrderStatus.DELIVERED) return;

            requireStatus(order, OrderStatus.DISPATCHED);

            shipmentService.markDelivered(shipmentId);
            order.setOrderStatus(OrderStatus.DELIVERED);

            orderRepo.save(order);
        } finally {
            lock.unlock();
        }
    }

    // -----------------------------
    // Cancel / Refund
    // -----------------------------

    @Override
    public void cancelOrder(String orderId, String reason) {
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("reason cannot be null/blank");

        ReentrantLock lock = lockForOrder(orderId);
        lock.lock();
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

            // Idempotency
            if (order.getOrderStatus() == OrderStatus.CANCELLED) return;
            if (order.getOrderStatus() == OrderStatus.REFUNDED) return;

            // Cannot cancel after dispatch/delivery (as per requirements)
            if (order.getOrderStatus() == OrderStatus.DISPATCHED || order.getOrderStatus() == OrderStatus.DELIVERED) {
                throw new RuntimeException("Cannot cancel after dispatch. orderId=" + orderId);
            }

            // If already paid, move to refund flow (inventory restore allowed because not dispatched)
            if (order.getOrderStatus() == OrderStatus.PAID ||
                    order.getOrderStatus() == OrderStatus.FULFILLMENT_CREATED) {
                order.setOrderStatus(OrderStatus.REFUND_PENDING);
                // Inventory restore policy: allowed only if not dispatched (we already blocked dispatch/delivered)
                inventoryService.releaseItems(order.getOrderItemList());
                orderRepo.save(order);
                return;
            }

            // Cancel before payment success
            if (order.getOrderStatus() == OrderStatus.RESERVED || order.getOrderStatus() == OrderStatus.PAYMENT_PENDING) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                inventoryService.releaseItems(order.getOrderItemList());
                orderRepo.save(order);
                return;
            }

            // Otherwise invalid
            throw new RuntimeException("Invalid state to cancel. orderId=" + orderId + ", state=" + order.getOrderStatus());

        } finally {
            lock.unlock();
        }
    }

    // -----------------------------
    // Helpers
    // -----------------------------

    private void requireStatus(Order order, OrderStatus expected) {
        if (order.getOrderStatus() != expected) {
            throw new RuntimeException("Invalid state. expected=" + expected + ", actual=" + order.getOrderStatus()
                    + ", orderId=" + order.getOrderId());
        }
    }
}