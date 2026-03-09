package Repository;

import Entity.Driver;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DriverRepository {
    private final  Map<String, Driver> driverMap = new ConcurrentHashMap<>();

    public void save(Driver driver){
        driverMap.put(driver.getDriverId(), driver);
    }

    public Map<String, Driver> findAll() {
        return Collections.unmodifiableMap(driverMap);
    }
    public List<Driver> getDriverList(){
        return List.copyOf(driverMap.values());
    }
    public Optional<Driver> findById(String driverId){
       return Optional.ofNullable(driverMap.get(driverId));
    }

}
