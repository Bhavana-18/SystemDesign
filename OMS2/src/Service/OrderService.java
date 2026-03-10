package Service;

import Entity.Order;
import Entity.OrderItem;

import java.util.List;

public interface OrderService {
    Order createOrder(String customerId, List<OrderItem> items);
    Order getOrder(String orderId);
    void confirmOrder(String orderId);
    void cancelOrder(String orderId);
    void startExpiryScheduler();
    void shutdownScheduler();
}
