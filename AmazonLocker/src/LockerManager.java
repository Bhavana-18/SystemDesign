import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LockerManager {
    List<Locker> lockerList ;

    public LockerManager (List<Locker> lockerList){
        this.lockerList = lockerList;
    }

    public void addLocker(Locker locker){
        lockerList.add(locker);
    }

    public List<Locker> getLockerList() {
        return lockerList;
    }

    public List<Locker> findAvailableLockers(Size size, String location){
        return lockerList.stream().filter(locker -> locker.getLockerSize() == size && locker.getStatus() == LockerStatus.AVAILABLE && locker.getLocation().equals(location)).collect(Collectors.toList());
    }

    public void assignLockerToPackage(Package pkg){
       for(Locker locker: lockerList){
           if(pkg.getPackageSize() == locker.getLockerSize() && locker.getStatus() == LockerStatus.AVAILABLE){
               locker.setStatus(LockerStatus.OCCUPIED);
               pkg.setLocker(locker);
               pkg.setOtp(generateOtp());
               return;
           }
       }
       throw new RuntimeException("No available Locker found");
    }

    public void releaseLocker(Locker locker) {
        locker.setStatus(LockerStatus.AVAILABLE);
    }

    private  String generateOtp(){
        return String.valueOf(new Random().nextInt(99999));
    }
}
