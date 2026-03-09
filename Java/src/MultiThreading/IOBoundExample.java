package MultiThreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class IOBoundExample {
    public static void main(String[] args){

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        ExecutorService executorService = Executors.newFixedThreadPool(2, runnable ->{
            Thread thread = new Thread(runnable);
            thread.setName("CustomThread-" + thread.threadId());
            return thread;
        });

        for(int i = 0; i<10; i++){
            int finalI = i;
            cachedThreadPool.execute(() ->{
                System.out.println(Thread.currentThread().getName() + " " + finalI);
                simulateWebRequest();
                System.out.println(Thread.currentThread().getName() + " completed I/O task." + finalI);
            });

        }
    }

    private static void simulateWebRequest(){
        try{
            System.out.println(Thread.currentThread().getName() + "is waiting for response");
            MILLISECONDS.sleep(700);
        } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
        }
    }
}
