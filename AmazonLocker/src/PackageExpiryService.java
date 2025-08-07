import java.time.Duration;
import java.time.LocalDateTime;


public class PackageExpiryService {
    private static final int TIMEOUT_DAYS = 3;
    LockerManager lockerManager;
    Payment payment;
    NotificationService notificationService;
    PackageExpiryService(LockerManager lockerManager, Payment payment, NotificationService notificationService){
        this.lockerManager = lockerManager;
        this.payment = payment;
        this.notificationService = notificationService;
    }

    public void processExpiredPackages(){

        for(Locker locker : lockerManager.getLockerList()){
           LocalDateTime cur = LocalDateTime.now();
            Package pkg = locker.getCurrentPkg();
            if(pkg != null && pkg.packageStatus != PackageStatus.PICKED_UP){
                Duration duration =Duration.between(cur, pkg.droppedTime);
                if(duration.toDays() > TIMEOUT_DAYS){
                    lockerManager.releaseLocker(locker);
                    pkg.setExpired(true);
                    payment.processRefund(pkg);
                    notificationService.notifyExpiryToUser(pkg);
                }

            }
        }
    }
}
