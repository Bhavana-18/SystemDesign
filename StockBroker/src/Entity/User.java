package Entity;

import Enums.UserStatus;

public class User {
    private final String userId;
    private String userName;
    private UserStatus status;
    public  User(String userId, String userName){
        if(userId == null || userId.isBlank()){
            throw new IllegalArgumentException("userId cannot be null/blank");
        }
        if(userName == null || userName.isBlank()){
            throw new IllegalArgumentException("userName cannot be null/blank");
        }
        this.userId = userId;
        this.userName = userName;
        this.status = UserStatus.ACTIVE;
    }

    public String getUserId() {
        return userId;
    }

    public void activate(){
        this.status = UserStatus.ACTIVE;
    }
    public void deactivate(){
        this.status = UserStatus.INACTIVE;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void ensureActive(){
        if(this.status != UserStatus.ACTIVE){
            throw new IllegalArgumentException("User is not Active");
        }
    }



}
