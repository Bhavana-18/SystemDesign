import java.time.Instant;
import java.util.List;
import java.util.Set;

class Tweet {
    private final String tweetId;
    private final User author;
    private final String content;
    private final Instant createdAt;

    private Set<User> likedBy;
    private Set<User> retweetedBy;

    private Tweet replyTo;
    private List<Tweet> replies;

    public void like(User user);
    public void unlike(User user);

    public void addReply(Tweet reply);
    public int getLikeCount();
}