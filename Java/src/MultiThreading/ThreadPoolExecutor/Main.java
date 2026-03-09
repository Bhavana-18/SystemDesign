package MultiThreading.ThreadPoolExecutor;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2),new CustomThreadFactory(), new CustomRejectHandler());
        executor.allowCoreThreadTimeOut(true);
        for(int i = 0; i<=6; i++){
            executor.submit(() ->{
                try{
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task Processed by:" + Thread.currentThread().getName());
            });
        }
        //Monitoring and Management facilities
        System.out.println("Active Threads" + executor.getActiveCount());
        System.out.println("Queued Tasks:" + executor.getQueue().size());

    }
}

class CustomThreadFactory implements ThreadFactory{
    @Override
    public Thread newThread(Runnable r){
        Thread th = new Thread(r);
        th.setDaemon(false);
        th.setPriority(Thread.NORM_PRIORITY);
        return th;
    }
}

class CustomRejectHandler implements RejectedExecutionHandler{
    @Override
    public void rejectedExecution(Runnable r , ThreadPoolExecutor threadPoolExecutor){
        System.out.println("Task rejected: " + r.toString());
    }
}
