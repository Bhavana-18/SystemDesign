package Repository;

import Entity.Shipment;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ShipmentRepository implements Repository<String, Shipment> {

    private final ConcurrentHashMap<String, Shipment> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Shipment> findById(String shipmentId) {
        return Optional.ofNullable(store.get(shipmentId));
    }

    @Override
    public void save(Shipment shipment) {
        store.put(shipment.getShipmentId(), shipment);
    }

    @Override
    public boolean existsById(String shipmentId) {
        return store.containsKey(shipmentId);
    }

    @Override
    public Optional<Shipment> deleteById(String shipmentId) {
        return Optional.ofNullable(store.remove(shipmentId));
    }

    @Override
    public Collection<Shipment> findAll() {
        return store.values();
    }

}