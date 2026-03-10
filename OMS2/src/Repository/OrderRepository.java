package Repository;

import Entity.Order;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository implements  Repository<String, Order> {
    private final ConcurrentHashMap<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Order> findById(String orderId){
        return Optional.ofNullable(store.get(orderId));
    }


    @Override
    public void save(Order order){
        store.put(order.getOrderId(), order);
    }

    @Override
    public boolean existsById(String orderId){
        return store.containsKey(orderId);
    }

    @Override
    public Optional<Order> deleteById(String orderId){
        return Optional.ofNullable(store.remove(orderId));
    }

    @Override
    public Collection<Order> findAll(){
        return store.values();
    }
}
