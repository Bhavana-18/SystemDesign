package Entity;

import Enums.DriverStatus;


import java.util.concurrent.locks.ReentrantLock;

public class Driver {
    private final String driverId;
    private final String driverName;
    private Location location;
    private final String email;
    private DriverStatus driverStatus;
    private final String license;
    private final ReentrantLock lock;

    public Driver(String driverId, String driverName,  String email, String license){
        this.driverId = driverId;
        this.driverName = driverName;
        this.email = email;
        this.license = license;
        this.lock = new ReentrantLock();
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public Location getLocation() {
        return location;
    }

    public String getDriverId() {
        return driverId;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public String getEmail() {
        return email;
    }

    public String getLicense() {
        return license;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDriverName() {
        return driverName;
    }
}
