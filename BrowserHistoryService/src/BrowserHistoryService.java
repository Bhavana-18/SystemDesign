import java.util.concurrent.ConcurrentHashMap;

public class BrowserHistoryService {
    private final ConcurrentHashMap<String, BrowserHistory> sessions = new ConcurrentHashMap<>();

    private final int maxHistorySize;
    public BrowserHistoryService(int maxHistorySize){
        this.maxHistorySize = maxHistorySize;
    }

    public void visit(String userId, String url){
        BrowserHistory history = sessions.computeIfAbsent(userId, id->new BrowserHistory(maxHistorySize));
        history.visit(url);
    }
    public String back(String userId) {
        BrowserHistory history = sessions.get(userId);
        return history == null ? null : history.back();
    }

    public String forward(String userId) {
        BrowserHistory history = sessions.get(userId);
        return history == null ? null : history.forward();
    }

    public void removeUser(String userId) {
        sessions.remove(userId);
    }

}
