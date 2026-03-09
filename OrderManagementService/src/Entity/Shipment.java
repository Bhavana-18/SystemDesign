package Entity;

import Enums.ShipmentStatus;

import java.time.Instant;
import java.util.Objects;

public class Shipment {
    private final String shipmentId;
    private final String orderId;
    private final String trackingId;

    private ShipmentStatus status;

    private final Instant createdAt;
    private Instant updatedAt;

    public Shipment(String shipmentId, String orderId, String trackingId) {
        this.shipmentId = Objects.requireNonNull(shipmentId);
        this.orderId = Objects.requireNonNull(orderId);
        this.trackingId = Objects.requireNonNull(trackingId);
        this.status = ShipmentStatus.CREATED;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getShipmentId() { return shipmentId; }
    public String getOrderId() { return orderId; }
    public String getTrackingId() { return trackingId; }
    public ShipmentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }


    public void setStatus(ShipmentStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
