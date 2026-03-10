package Repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<ID, T> {
    Optional<T> findById(ID id);
    void save(T entity);
    boolean existsById(ID id);
    Optional<T> deleteById(ID id);
    Collection<T> findAll();

}
