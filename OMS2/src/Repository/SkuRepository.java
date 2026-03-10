package Repository;

import Entity.StockKeepingUnit;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SkuRepository  implements Repository<String, StockKeepingUnit> {
    private final ConcurrentHashMap<String, StockKeepingUnit> store = new ConcurrentHashMap<>();

    @Override
    public Optional<StockKeepingUnit> findById(String skuId){
        return Optional.ofNullable(store.get(skuId));
    }

    @Override
    public void save(StockKeepingUnit stockKeepingUnit){
        store.put(stockKeepingUnit.getSkuId(), stockKeepingUnit);
    }

    @Override
    public boolean existsById(String skuId){
        return store.containsKey(skuId);
    }

    @Override
    public Optional<StockKeepingUnit> deleteById(String skuId){
        return Optional.ofNullable(store.remove(skuId));
    }

    @Override
    public Collection<StockKeepingUnit> findAll(){
        return  store.values();
    }
}
