package Service;

import java.util.PriorityQueue;

public class TimelineServiceImpl  implements TimeLineService{

    public List<Tweet> getHomeTimeline(User user) {
        PriorityQueue<Tweet> maxHeap =
                new PriorityQueue<>((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        for (User followee : user.getFollowing()) {
            List<Tweet> tweets = followee.getTweets();
            if (!tweets.isEmpty()) {
                maxHeap.add(tweets.get(0));
            }
        }

        List<Tweet> result = new ArrayList<>();

        while (!maxHeap.isEmpty() && result.size() < 20) {
            Tweet latest = maxHeap.poll();
            result.add(latest);
        }

        return result;
    }

}
class EngagementServiceImpl implements EngagementService {

    @Override
    public void like(User user, Tweet tweet) {
        tweet.like(user);
    }
}

class TweetServiceImpl implements TweetService {

    private TweetRepository tweetRepo;

    @Override
    public Tweet createTweet(User author, String content) {
        Tweet tweet = new Tweet(UUID.randomUUID(), author, content);
        tweetRepo.save(tweet);
        return tweet;
    }
}


