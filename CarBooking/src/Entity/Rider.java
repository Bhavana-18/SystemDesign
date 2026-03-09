package Entity;

import java.util.concurrent.locks.ReentrantLock;

public class Rider {
    private final String riderId;
    private final String userName;
    private String activeRideId;
    private final ReentrantLock lock;

    public Rider(String riderId, String userName){
        this.riderId = riderId;
        this.lock = new ReentrantLock();
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public String getRiderId() {
        return riderId;
    }

    public String getActiveRideId() {
        return activeRideId;
    }

    public void setActiveRideId(String activeRideId) {
        this.activeRideId = activeRideId;
    }
}
