public class NotificationService {
    public void sendOtpToUser(Package pkg){
        System.out.println("your otp for pickup " + pkg.getOtp());
    }
    public void notifyUsers(Package pkg){
        System.out.println(" Dear " + pkg.getUser() + " your package" + pkg.getPackageId() + "dropped" );
    }
    public void notifyExpiryToUser(Package pkg) {
        // Notification logic
        System.out.println("User notified for unpicked package: " + pkg.getPackageId());
    }
}
