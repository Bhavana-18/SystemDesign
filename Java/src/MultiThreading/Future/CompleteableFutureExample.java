package MultiThreading.Future;

import java.util.concurrent.*;

public class CompleteableFutureExample {

public static void main (String[] args) {
    try {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, new ArrayBlockingQueue<>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<String> asyncTask1 = CompletableFuture.supplyAsync(() -> {
            return "task completed";
        }, poolExecutor).thenApply((String val) ->{
            return  val + " successfully";
        });

        // chaining is supported in CompletableFuture
        //then Apply is a synchronous execution
        System.out.println(asyncTask1.get());

        CompletableFuture<String> asyncTask2 = CompletableFuture.supplyAsync(() ->{
            try{
              System.out.println("Executed thread" + Thread.currentThread().getName());
                return "task completed 1";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }).thenApply((String val)->{
            System.out.println("Executed thread thenApply on" + Thread.currentThread().getName());
            return  val + "and";

        });

        System.out.println(asyncTask2.get());
     // thenCombine chain together dependent Async operations
        CompletableFuture<String> compFutureObj = CompletableFuture.supplyAsync(() ->{
            return "hello";
        }, poolExecutor).thenComposeAsync((String val) ->{
           return CompletableFuture.supplyAsync(() -> {return  val + "world";}) ;
        });
           CompletableFuture<Void> comp =    compFutureObj .thenAccept((String val) ->{
            System.out.println("Printing val " + val);
        });
         //thencombine, thenApply , these are synchronous actions
        //thenCombineAsync - in new thread they're executed

        // the ordering is maintained
        CompletableFuture<String> asyncTask3 = CompletableFuture.supplyAsync(() ->{
            return "k";
        });
        CompletableFuture<Integer> asyncTask4 = CompletableFuture.supplyAsync(() ->{
            return 10;
        });
        CompletableFuture<String> asyncTask = asyncTask3.thenCombine(asyncTask4, ( String val2, Integer val1) -> val1+ val2);

        System.out.println(asyncTask.get());

        System.out.println(compFutureObj.get());


    } catch (Exception e) {
        throw new RuntimeException(e);
    }

}
}
