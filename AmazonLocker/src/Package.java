public class Package {
    int packageId;
    Size packageSize;
    User user;
    Locker locker;
    PackageStatus packageStatus;
    String otp;

    Package(int packageId, Size packageSize, User user){
        this.packageId = packageId;
        this.packageSize = packageSize;
        this.user = user;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setPackageStatus(PackageStatus packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getOtp() {
        return otp;
    }

    public Size getPackageSize() {
        return packageSize;
    }

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public User getUser() {
        return user;
    }

    public int getPackageId() {
        return packageId;
    }

    public Locker getLocker() {
        return locker;
    }
}
