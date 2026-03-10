package Entity;

import Enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final String orderId;
    private final String customerId;

    private OrderStatus orderStatus;
    private final List<OrderItem> orderItemList;

    private final Instant createdAt;
    private final Instant expiresAt;
    private Instant updatedAt;

    private BigDecimal totalAmount;

    public Order(String orderId,
                 String customerId,
                 OrderStatus orderStatus,
                 List<OrderItem> orderItemList,
                 BigDecimal total) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.orderItemList = new ArrayList<>(orderItemList);
        this.createdAt = Instant.now();
        this.expiresAt = createdAt.plusSeconds(5 * 60);
        this.updatedAt = createdAt;
        this.totalAmount = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItem> getOrderItemList() {
        return Collections.unmodifiableList(orderItemList);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void transitionTo(OrderStatus newStatus) {
        this.orderStatus = newStatus;
        this.updatedAt = Instant.now();
    }
}