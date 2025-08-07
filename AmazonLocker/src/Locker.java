public class Locker {
    private  final int LockerId;
    private final String location;
   private final  Size lockerSize;
    private LockerStatus status;
    private Package currentPkg;

    Locker(int LockerId, String location , Size lockerSize){
        this.LockerId = LockerId;
        this.location = location;
        this.lockerSize = lockerSize;
        this.status = LockerStatus.AVAILABLE;
        this.currentPkg = null;
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

    public Package getCurrentPkg() {
        return currentPkg;
    }

    public void setCurrentPkg(Package currentPkg) {
        this.currentPkg = currentPkg;
    }
}

