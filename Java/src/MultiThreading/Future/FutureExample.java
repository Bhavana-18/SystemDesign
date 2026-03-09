package MultiThreading.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FutureExample {
    public static void main (String[] args) {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        // this is runnable
        Future<?> future = poolExecutor.submit(() -> {
            try{
                Thread.sleep(7000);
                System.out.println("this is the task which thread will execute");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        });
        // this is callable
        Future<Integer> future1 = poolExecutor.submit(()->{
            System.out.println("do something");
            return 45;
        });
        System.out.println("isDone:" +future.isDone());

        try{
            future.get(2, TimeUnit.SECONDS);
        } catch(TimeoutException e){
            System.out.println("Timeout Exception Happened");
        } catch(Exception e){

        }

        try{
            Object object = future.get();
            System.out.println(object == null);
        } catch(Exception e){

        }

        List<Integer>output = new ArrayList<>();
        //submit(Runnable task, T result)
        Future<List<Integer>> future2 = poolExecutor.submit(new MyRunnable(output),output);
        try{
            future2.get();
            System.out.println(output.get(0));
            List<Integer> result = future2.get();
            System.out.println(result.get(0));
        } catch (Exception e){

        }
    }


}
