package CounterScheduler;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TimeLimitedCounter {

    static class Entry {
        Long timeStamp;
        String key;

        Entry(Long timeStamp, String key) {
            this.timeStamp = timeStamp;
            this.key = key;

        }
    }
        private final Deque<Entry> timeBuffer = new ArrayDeque<>();
        private final Map<String, Integer> counter = new HashMap<>();

        private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        private final long timeWindowMillis ;
        private final ReentrantLock lock = new ReentrantLock();


        TimeLimitedCounter(long timeWindow , long cleanUpIntervalMillis){

            this.timeWindowMillis = timeWindow *1000;
            scheduler.scheduleAtFixedRate(this::runScheduler, cleanUpIntervalMillis, cleanUpIntervalMillis, TimeUnit.MILLISECONDS );
        }

        public void runScheduler(){

            long currentTimeStamp = System.currentTimeMillis();
            lock.lock();
            try {

                while (!timeBuffer.isEmpty() && currentTimeStamp - timeBuffer.peek().timeStamp > timeWindowMillis) {
                    Entry entry = timeBuffer.poll();
                    counter.put(entry.key, counter.get(entry.key) - 1);
                    if (counter.get(entry.key) == 0)
                        counter.remove(entry.key);
                }
            }
            finally{
                lock.unlock();
            }

       }

       public int get(String key){
            lock.lock();
            try{
                return counter.getOrDefault(key, 0);
            } finally {
                lock.unlock();
            }
       }
       public int getTotalCount(){
            lock.lock();
            try{
                return timeBuffer.size();
            } finally {
                lock.unlock();
            }
       }

       public void put(String key){
            Long timeStamp = System.currentTimeMillis();
            lock.lock();
            try{
                counter.put(key, counter.getOrDefault(key, 0) +1);
                timeBuffer.add(new Entry(timeStamp, key));

            } finally{
                lock.unlock();
            }
       }

    public void shutDown(){
            scheduler.shutdown();
    }
}
