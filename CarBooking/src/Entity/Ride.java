package Entity;

import Enums.RideStatus;

import java.util.concurrent.locks.ReentrantLock;

public class Ride {
    private final String rideId;
    private final String driverId;
    private final String riderId;
    private Location start;
    private Location end;
    private  double fair;
    private RideStatus rideStatus;
    private final ReentrantLock lock;

    public Ride(String rideId, String riderId, String driverId, Location start, Location end, double fair){
        this.driverId = driverId;
        this.rideId = rideId;
        this.riderId = riderId;
        this.lock = new ReentrantLock();
        this.rideStatus = RideStatus.ASSIGNED;
        this.start = start;
        this.end = end;
        this.fair = fair;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RideStatus rideStatus) {
        this.rideStatus = rideStatus;
    }
    public Location getStart(){
        return start;
    }

    public Location getEnd(){
        return end;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getRideId() {
        return rideId;
    }

    public double getFair() {
        return fair;
    }
    public void setFair(long fair){
        this.fair = fair;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setEnd(Location end) {
        this.end = end;
    }
}
