public class User {
//    private final int userId;
    private final String userName;
    private final String contactInfo;
    private final String driversLicense;

    User(String userName, String contactInfo , String driversLicense){
        this.contactInfo = contactInfo;
        this.userName = userName;
        this.driversLicense = driversLicense;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDriversLicense() {
        return driversLicense;
    }

    public String getUserName() {
        return userName;
    }
}

