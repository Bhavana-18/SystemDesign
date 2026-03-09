import Entity.Order;
import Entity.OrderItem;
import Entity.StockKeepingUnit;
import Enums.PaymentMode;
import Enums.ProductStatus;
import Repository.*;
import Service.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class OrderManagementApplication {

    public static void main(String[] args) {

        // -----------------------
        // Repositories
        // -----------------------
        SkuRepository skuRepository = new SkuRepository();
        OrderRepository orderRepository = new OrderRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        ShipmentRepository shipmentRepository = new ShipmentRepository();
        InventoryRepository inventoryRepository = new InventoryRepository();

        // -----------------------
        // Services
        // -----------------------
        InventoryService inventoryService =
                new InventoryServiceImpl(inventoryRepository);

        PaymentService paymentService =
                new PaymentServiceImpl(paymentRepository);

        ShipmentService shipmentService =
                new ShipmentServiceImpl(shipmentRepository);

        OrderService orderService =
                new OrderServiceImpl(
                        orderRepository,
                        skuRepository,
                        inventoryService,
                        paymentService,
                        shipmentService
                );

        // -----------------------
        // Setup SKUs
        // -----------------------
        StockKeepingUnit sku1 = new StockKeepingUnit("SKU-1", "Phone", BigDecimal.valueOf(10000), ProductStatus.ACTIVE);
        StockKeepingUnit sku2 = new StockKeepingUnit("SKU-2", "Headphones", BigDecimal.valueOf(2000), ProductStatus.ACTIVE);

        skuRepository.save(sku1);
        skuRepository.save(sku2);

        // Add stock
        inventoryService.addStock("SKU-1", 21);
        inventoryService.addStock("SKU-2", 5);

        // -----------------------
        // Create Order
        // -----------------------
        List<OrderItem> items = Arrays.asList(
                new OrderItem("SKU-1", 2,BigDecimal.valueOf(10000)),
                new OrderItem("SKU-2", 1,BigDecimal.valueOf(10000))
        );

        Order order = orderService.createOrder("CUST-1", items);
        System.out.println("Order created: " + order.getOrderId());
        System.out.println("Order Status: " + order.getOrderStatus());

        // -----------------------
        // Payment Flow
        // -----------------------
        var payment = orderService.initiatePayment(order.getOrderId(), PaymentMode.UPI);
        System.out.println("Payment initiated: " + payment.getPaymentId());

        orderService.confirmPayment(order.getOrderId(), payment.getPaymentId(), "EXT-REF-123");
        System.out.println("Order Status after payment: " +
                orderService.getOrder(order.getOrderId()).getOrderStatus());

        // -----------------------
        // Shipment Flow
        // -----------------------
        var shipment = orderService.createShipment(order.getOrderId());
        System.out.println("Shipment created: " + shipment.getShipmentId());

        orderService.dispatch(shipment.getShipmentId());
        System.out.println("Order Status after dispatch: " +
                orderService.getOrder(order.getOrderId()).getOrderStatus());

        orderService.deliver(shipment.getShipmentId());
        System.out.println("Order Status after delivery: " +
                orderService.getOrder(order.getOrderId()).getOrderStatus());

        // -----------------------
        // Final Output
        // -----------------------
        Order finalOrder = orderService.getOrder(order.getOrderId());
        System.out.println("Final Order Status: " + finalOrder.getOrderStatus());
        System.out.println("Total Amount: " + finalOrder.getTotalAmount());
        Runnable r = () -> {
            try {
                orderService.createOrder("CUST-1",
                        List.of(new OrderItem("SKU-1", 1, BigDecimal.valueOf(1000))));
            } catch (Exception e) {
                System.out.println("Failed: " + e.getMessage());
            }
        };

        for (int i = 0; i < 20; i++) {
            new Thread(r).start();
        }
    }
}
