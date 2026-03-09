package Repository;

import Entity.Ride;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RideRepository {

    Map<String, Ride> rideMap = new ConcurrentHashMap<>();

    public void save(Ride ride){
        rideMap.put(ride.getRideId(), ride);
    }

    public Map<String, Ride> findAll() {
        return Collections.unmodifiableMap(rideMap);
    }
    public Optional<Ride> findById(String rideId){
        if(rideMap.containsKey(rideId))
            return Optional.of(rideMap.get(rideId));
        return Optional.empty();
    }
}
