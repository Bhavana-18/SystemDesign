package Service;

public interface FollowService {
    void follow(User follower, User followee);
    void unfollow(User follower, User followee);
}
