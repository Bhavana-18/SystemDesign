public class PackageManager {
    LockerManager lockerManager;
    NotificationService notificationService;

    PackageManager(LockerManager lockerManager, NotificationService notificationService){
       this.lockerManager = lockerManager;
       this.notificationService = notificationService;
    }
    public void dropPackage(Package pkg){
      lockerManager.assignLockerToPackage(pkg);
      pkg.setPackageStatus(PackageStatus.STORED);
      notificationService.sendOtpToUser(pkg);
      notificationService.notifyUsers(pkg);

    }

    public boolean pickupPackage(Package pkg, String otp){

        if(pkg.getOtp().equals(otp)){
            Locker locker = pkg.getLocker();
           lockerManager.releaseLocker(locker);
           pkg.setPackageStatus(PackageStatus.PICKED_UP);
           System.out.println("Picked up");
           return true;

        }
     return  false;
    }



}
