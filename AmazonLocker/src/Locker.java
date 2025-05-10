public class Locker {
    private  final int LockerId;
    private final String location;
   private final  Size lockerSize;
    private LockerStatus status;

    Locker(int LockerId, String location , Size lockerSize){
        this.LockerId = LockerId;
        this.location = location;
        this.lockerSize = lockerSize;
        this.status = LockerStatus.AVAILABLE;
    }

    public Size getLockerSize() {
        return lockerSize;
    }

    public LockerStatus getStatus() {
        return status;
    }

    public void setStatus(LockerStatus status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public int getLockerId() {
        return LockerId;
    }
}

