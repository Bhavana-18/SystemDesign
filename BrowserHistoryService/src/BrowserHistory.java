import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.ReentrantLock;

public class BrowserHistory {
    private final Deque<String> backHistory = new ArrayDeque<>();
    private final Deque<String> forwardHistory = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final int maxHistorySize;

    private String currentUrl;
    BrowserHistory(int maxHistorySize){
        this.maxHistorySize = maxHistorySize;
    }

    public void visit(String url){
        lock.lock();
        try{
            if(currentUrl != null){
                backHistory.add(currentUrl);
                if(backHistory.size() > maxHistorySize){
                    backHistory.removeFirst();
                }

            }
            currentUrl = url;
            forwardHistory.clear();

        } finally {
            lock.unlock();
        }
    }

    public String back(){
        lock.lock();
        try{
            if(backHistory.isEmpty())
                return currentUrl;
            forwardHistory.add(currentUrl);
            currentUrl = backHistory.removeLast();
            return currentUrl;

        } finally {
            lock.unlock();
        }
    }

    public String forward(){
        lock.lock();
        try{
            if(forwardHistory.isEmpty())
                return currentUrl;
            backHistory.add(currentUrl);
            currentUrl = forwardHistory.removeLast();
            return currentUrl;
        } finally {
            lock.unlock();
        }
    }
}

