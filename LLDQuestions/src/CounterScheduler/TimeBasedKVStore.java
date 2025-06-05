package CounterScheduler;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TimeBasedKVStore {
    private final ConcurrentHashMap<String, List<ValueEntry>> store = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<TimeStampedKey> expiryQueue = new ConcurrentLinkedQueue<>();
    private final long ttlMillis;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static class ValueEntry {
        String value;
        long timestamp;

        ValueEntry(String value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    private static class TimeStampedKey {
        String key;
        long timestamp;

        TimeStampedKey(String key, long timestamp) {
            this.key = key;
            this.timestamp = timestamp;
        }
    }

    public TimeBasedKVStore(long ttlMillis) {
        this.ttlMillis = ttlMillis;
        startCleanupScheduler();
    }

    public void put(String key, String value) {
        long now = System.currentTimeMillis();
        lock.writeLock().lock();
        try {
            store.computeIfAbsent(key, k -> new ArrayList<>()).add(new ValueEntry(value, now));
            expiryQueue.add(new TimeStampedKey(key, now));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<String> get(String key) {
        long now = System.currentTimeMillis();
        lock.readLock().lock();
        try {
            List<String> result = new ArrayList<>();
            List<ValueEntry> entries = store.getOrDefault(key, new ArrayList<>());
            for (ValueEntry entry : entries) {
                if (now - entry.timestamp <= ttlMillis) {
                    result.add(entry.value);
                }
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void startCleanupScheduler() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::cleanup, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        lock.writeLock().lock();
        try {
            while (!expiryQueue.isEmpty()) {
                TimeStampedKey tsKey = expiryQueue.peek();
                if (now - tsKey.timestamp > ttlMillis) {
                    expiryQueue.poll(); // remove from queue
                    List<ValueEntry> list = store.get(tsKey.key);
                    if (list != null) {
                        list.removeIf(entry -> entry.timestamp == tsKey.timestamp);
                        if (list.isEmpty()) store.remove(tsKey.key);
                    }
                } else {
                    break;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
