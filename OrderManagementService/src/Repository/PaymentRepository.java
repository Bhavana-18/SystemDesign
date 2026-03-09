package Repository;

import Entity.Payment;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentRepository implements Repository<String, Payment> {

    private final ConcurrentHashMap<String, Payment> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Payment> findById(String paymentId) {
        return Optional.ofNullable(store.get(paymentId));
    }

    @Override
    public void save(Payment payment) {
        store.put(payment.getPaymentId(), payment);
    }

    @Override
    public boolean existsById(String paymentId) {
        return store.containsKey(paymentId);
    }

    @Override
    public Optional<Payment> deleteById(String paymentId) {
        return Optional.ofNullable(store.remove(paymentId));
    }

    @Override
    public Collection<Payment> findAll() {
        return store.values();
    }
}