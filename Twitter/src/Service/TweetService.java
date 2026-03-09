package Service;

public interface TweetService {
    Tweet createTweet(User author, String content);
    Tweet reply(User author, Tweet parent, String conten
}
