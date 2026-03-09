package Service;

import Entity.Driver;
import Entity.Location;
import Enums.DriverStatus;

import java.util.List;

public class NearestDriverMatchingStrategy implements MatchingStrategy{

    @Override
    public Driver findBestDriver(Location pickup, List<Driver> drivers){
        Driver bestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for(Driver driver : drivers){
            if(driver.getDriverStatus() != DriverStatus.AVAILABLE)
                continue;
            if(driver.getLocation() == null)
                continue;
            double distance = driver.getLocation().distanceTo(pickup);
            if (distance < minDistance) {
                minDistance = distance;
                bestDriver = driver;
            }
        }
        return  bestDriver;
    }


}
