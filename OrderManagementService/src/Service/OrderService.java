package Service;

import Entity.Order;
import Entity.OrderItem;
import Entity.Payment;
import Entity.Shipment;
import Enums.PaymentMode;

import java.util.List;

public interface OrderService {

    // Order
    Order createOrder(String customerId, List<OrderItem> items);
    Order getOrder(String orderId);
    List<Order> getOrdersByCustomer(String customerId);

    // Payment
    Payment initiatePayment(String orderId, PaymentMode mode);
    void confirmPayment(String orderId, String paymentId, String externalRef);
    void failPayment(String orderId, String paymentId, String reason);

    // Shipment
    Shipment createShipment(String orderId);
    void dispatch(String shipmentId);
    void deliver(String shipmentId);

    // Cancellation
    void cancelOrder(String orderId, String reason);
}
