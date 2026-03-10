package Repository;

import Entity.InventoryRecord;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryRepository implements Repository<String, InventoryRecord> {
    private final ConcurrentHashMap<String, InventoryRecord> store = new ConcurrentHashMap<>();
    @Override
    public Optional<InventoryRecord> findById(String skuId) {
        return Optional.ofNullable(store.get(skuId));
    }

    @Override
    public void save(InventoryRecord record) {
        store.put(record.getSkuId(), record);
    }

    @Override
    public boolean existsById(String skuId) {
        return store.containsKey(skuId);
    }

    @Override
    public Optional<InventoryRecord> deleteById(String skuId) {
        return Optional.ofNullable(store.remove(skuId));
    }

    @Override
    public Collection<InventoryRecord> findAll() {
        return store.values();
    }
}
