package MultiThreading;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ShutDown {
    public static void main(String[] args) throws InterruptedException{


        ExecutorService executors = Executors.newFixedThreadPool(3);
        for(int i = 0 ; i<5; i++) {
            int finalI = i;
            executors.submit(() -> {
                try {

                    System.out.println("executed in thread pool");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("task " + finalI + " completed");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        TimeUnit.SECONDS.sleep(1);


        executors.shutdown(); // it will not interrupt the actively executing task
        //executors.shutdownNow();
        //shutdown Now will interrupt the actively executing task
        try{
            boolean isTerminated = executors.awaitTermination(2, TimeUnit.SECONDS);
            System.out.println("isTerminated" + isTerminated);
//            if(!isTerminated)
              List<Runnable> pendingTasks =   executors.shutdownNow();
              System.out.println("Is shutdown: " + executors.isShutdown());
              System.out.println("Number of pending tasks that never started" + pendingTasks.size());
        } catch (InterruptedException e){

        }
        System.out.println("Main thread stopped executing");

    }
}
