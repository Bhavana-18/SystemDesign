package Repository;

import Entity.Rider;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RiderRepository {
    Map<String, Rider> riderMap = new ConcurrentHashMap<>();

    public void save(Rider rider){
        riderMap.put(rider.getRiderId(), rider);
    }

    public Map<String, Rider> findAll() {
        return Collections.unmodifiableMap(riderMap);
    }
    public Optional<Rider> findById(String riderId){
        if(riderMap.containsKey(riderId))
            return Optional.of(riderMap.get(riderId));
        return Optional.empty();
    }
}
