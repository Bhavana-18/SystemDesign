import java.time.LocalDateTime;

public class Package {
    int packageId;
    Size packageSize;
    User user;
    LocalDateTime droppedTime;
    PackageStatus packageStatus;
    String otp;
    boolean isExpired;

    Package(int packageId, Size packageSize, User user, LocalDateTime droppedTime){
        this.packageId = packageId;
        this.packageSize = packageSize;
        this.user = user;
        this.droppedTime = droppedTime;
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



    public User getUser() {
        return user;
    }

    public int getPackageId() {
        return packageId;
    }



    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }


}
