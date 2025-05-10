public class NotificationService {
    public void sendOtpToUser(Package pkg){
        System.out.println("your otp for pickup " + pkg.getOtp());
    }
    public void notifyUsers(Package pkg){
        System.out.println(" Dear " + pkg.getUser() + " your package" + pkg.getPackageId() + "dropped" + pkg.getLocker().getLocation());
    }
}
