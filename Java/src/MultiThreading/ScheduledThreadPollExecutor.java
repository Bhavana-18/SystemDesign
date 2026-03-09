package MultiThreading;

import java.util.concurrent.*;

public class ScheduledThreadPollExecutor {
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
       Future<String> futureObj = executorService.schedule(() ->{
            System.out.println("hello");
            return "hello";
        }, 2, TimeUnit.SECONDS);   // it will run after 2 seconds
       Future<?> fO=  executorService.scheduleAtFixedRate(() ->{
            System.out.println("this will run in loop with goven interval;");
        }, 5, 6, TimeUnit.SECONDS);
    }




}
