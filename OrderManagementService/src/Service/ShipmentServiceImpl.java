package Service;

import Entity.Shipment;
import Enums.ShipmentStatus;
import Repository.ShipmentRepository;

import java.util.Objects;
import java.util.UUID;

public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = Objects.requireNonNull(shipmentRepository);
    }

    @Override
    public Shipment create(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId cannot be null/blank");
        }

        String shipmentId = UUID.randomUUID().toString();
        String trackingId = "TRK-" + UUID.randomUUID(); // simple tracking generator

        Shipment shipment = new Shipment(shipmentId, orderId, trackingId);
        shipmentRepository.save(shipment);
        return shipment;
    }

    @Override
    public void markDispatched(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException("shipmentId cannot be null/blank");
        }

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + shipmentId));

        // Idempotency
        if (shipment.getStatus() == ShipmentStatus.DISPATCHED) {
            return;
        }
        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
            throw new RuntimeException("Shipment already DELIVERED; cannot dispatch. shipmentId=" + shipmentId);
        }
        if (shipment.getStatus() != ShipmentStatus.CREATED) {
            throw new RuntimeException("Invalid shipment state to dispatch. shipmentId=" + shipmentId +
                    ", state=" + shipment.getStatus());
        }

        shipment.setStatus(ShipmentStatus.DISPATCHED);
        shipmentRepository.save(shipment);
    }

    @Override
    public void markDelivered(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException("shipmentId cannot be null/blank");
        }

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + shipmentId));

        // Idempotency
        if (shipment.getStatus() == ShipmentStatus.DELIVERED) {
            return;
        }
        if (shipment.getStatus() != ShipmentStatus.DISPATCHED) {
            throw new RuntimeException("Invalid shipment state to deliver. shipmentId=" + shipmentId +
                    ", state=" + shipment.getStatus());
        }

        shipment.setStatus(ShipmentStatus.DELIVERED);
        shipmentRepository.save(shipment);
    }

    @Override
    public Shipment get(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) {
            throw new IllegalArgumentException("shipmentId cannot be null/blank");
        }
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found: " + shipmentId));
    }
}