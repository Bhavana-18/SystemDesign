import java.util.Set;

public class User {
    private final String userId;
    private String username;
    private String bio;

    private Set<User> followers;
    private Set<User> following;

    private List<Tweet> tweets;

    public void follow(User user);
    public void unfollow(User user);

    public Tweet postTweet(String text);
    public void likeTweet(Tweet tweet);
    public void retweet(Tweet tweet);

    public List<Tweet> getTimeline() {
        return null;
    }
}
