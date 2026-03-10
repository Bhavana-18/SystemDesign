import Entity.Order;
import Entity.OrderItem;
import Entity.StockKeepingUnit;

import Enums.ProductStatus;
import Repository.InventoryRepository;
import Repository.OrderRepository;
import Repository.SkuRepository;
import Service.InventoryService;
import Service.InventoryServiceImpl;
import Service.OrderService;
import Service.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        SkuRepository skuRepo = new SkuRepository();
        OrderRepository orderRepo = new OrderRepository();
        InventoryRepository inventoryRepo = new InventoryRepository();

        InventoryService inventoryService = new InventoryServiceImpl(inventoryRepo);
        OrderService orderService = new OrderServiceImpl(orderRepo, skuRepo, inventoryService);

        skuRepo.save(new StockKeepingUnit("P1", "Phone", new BigDecimal("10000"), ProductStatus.ACTIVE));
        skuRepo.save(new StockKeepingUnit("P2", "Headphones", new BigDecimal("2000"), ProductStatus.ACTIVE));

        inventoryService.addStock("P1", 10);
        inventoryService.addStock("P2", 5);

        orderService.startExpiryScheduler();

        Order order = orderService.createOrder("C1", List.of(
                new OrderItem("P1", 2, new BigDecimal("10000")),
                new OrderItem("P2", 1, new BigDecimal("10000"))
        ));

        System.out.println("Created order: " + order.getOrderId());
        System.out.println("Status: " + order.getOrderStatus());
        System.out.println("P1 available after reserve: " + inventoryService.getAvailableStock("P1"));

        orderService.confirmOrder(order.getOrderId());

        Order confirmed = orderService.getOrder(order.getOrderId());
        System.out.println("Status after confirm: " + confirmed.getOrderStatus());
        System.out.println("P1 available after confirm: " + inventoryService.getAvailableStock("P1"));

        orderService.shutdownScheduler();
    }
}