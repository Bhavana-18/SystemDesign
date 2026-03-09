package Service;

import Entity.Driver;
import Entity.Location;
import Enums.DriverStatus;
import Repository.DriverRepository;
import Exception.DriverNotFoundException;

import java.util.UUID;

public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository){
        this.driverRepository = driverRepository;
    }
    public void updateDriverLocation(String driverId, Location location){
        Driver driver = getDriverOrThrow(driverId);
        driver.getLock().lock();
        try{
            driver.setLocation(location);
            driverRepository.save(driver);

        } finally{
            driver.getLock().unlock();
        }

    }

    public Driver registerDriver(String name, String email, String license){
        if(name == null || email == null || license == null || name.isBlank()|| email.isBlank()|| license.isBlank()) {
            throw new IllegalArgumentException("Email or Name or license cannot be empty");
        }
            String driverId = UUID.randomUUID().toString();
            Driver driver = new Driver(driverId, name,email,license);
            driverRepository.save(driver);
            return driver;

    }

    public void updateDriverStatus(String driverId, DriverStatus driverStatus){
        Driver driver = getDriverOrThrow(driverId);
        driver.getLock().lock();
        try{
            driver.setDriverStatus(driverStatus);
            driverRepository.save(driver);

        } finally{
            driver.getLock().unlock();
        }

    }

    public Driver getDriverOrThrow(String driverId){
        if(driverId == null || driverId.isBlank()){
            throw new IllegalArgumentException("DriverId cannot be blank or null");
        }
        return driverRepository.findById(driverId).orElseThrow(()-> new DriverNotFoundException("Driver not found"));
    }
}
