package Entity;

import Enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
   private final  String orderId;
   private final String customerId;
    private String paymentId;
   private  String shipmentId;

   private OrderStatus orderStatus;
   private List<OrderItem> orderItemList;
   private final Instant createdAt;
   private Instant updatedAt;

   private BigDecimal totalAmount;

   public Order(String orderId,String customerId, OrderStatus orderStatus, List<OrderItem> orderItemList, BigDecimal total){
       this.orderId = orderId;
       this.customerId = customerId;
       this.orderStatus = orderStatus;
       this.orderItemList = new ArrayList<>(orderItemList);
       this.createdAt = Instant.now();
       this.updatedAt = Instant.now();
       this.totalAmount =total;
   }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<OrderItem> getOrderItemList() {
        return Collections.unmodifiableList(orderItemList);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void transitionTo(OrderStatus newStatus) {
       //validate allowed transitions
        this.orderStatus = orderStatus;
        this.updatedAt = Instant.now();
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
        this.updatedAt = Instant.now();
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        this.updatedAt = Instant.now();
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
        this.updatedAt = Instant.now();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getCustomerId() {
        return customerId;
    }
}
