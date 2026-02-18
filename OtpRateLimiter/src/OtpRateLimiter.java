import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class OtpRateLimiter {
    private final int maxRequestsAllowed;
    private final long windowMillis;
    ConcurrentHashMap<String, Deque<Long>> attempts = new ConcurrentHashMap<>();

    OtpRateLimiter(int maxRequestsAllowed , Duration window){
        this.maxRequestsAllowed = maxRequestsAllowed;
        windowMillis = window.toMillis();
    }

    public boolean allowRequest(String userId){

        AtomicBoolean allow = new AtomicBoolean(false);
        attempts.compute(userId, (key, deque) ->{
            long curr = System.currentTimeMillis();
            if(deque == null)
                deque = new ArrayDeque<>();

            while(!deque.isEmpty() && curr - deque.peekFirst() > windowMillis){
                deque.pollFirst();
            }
            if(deque.size() < maxRequestsAllowed){
                allow.set(true);
                deque.addLast(curr);
            }

            if(deque.isEmpty())
                return  null;
            return  deque;
        });
        return allow.get();

    }
}
