package Service;

import Entity.Shipment;

public interface ShipmentService {
    Shipment create(String orderId);
    void markDispatched(String shipmentId);
    void markDelivered(String shipmentId);
    Shipment get(String shipmentId);
}
