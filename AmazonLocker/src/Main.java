import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        Locker locker1 = new Locker(1, "Hyd", Size.LARGE);
        Locker locker2 = new Locker(2, "Hyd", Size.SMALL);
        Locker locker3 = new Locker(3, "Hyd", Size.MEDIUM);
        List<Locker> lockerList = new ArrayList<>();
        lockerList.add(locker1); lockerList.add(locker2); lockerList.add(locker3);
        LockerManager lockerManager = new LockerManager(lockerList);
        NotificationService notificationService = new NotificationService();

        PackageManager packageManager = new PackageManager(lockerManager,notificationService);
        User user1 = new User("124", "Bhavana", "999999999", "Hyd");
        Package pkg = new Package(1, Size.SMALL,user1 );
        packageManager.dropPackage(pkg);
        packageManager.pickupPackage(pkg,"123");
    }
}