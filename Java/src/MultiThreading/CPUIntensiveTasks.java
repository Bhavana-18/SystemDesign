package MultiThreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CPUIntensiveTasks {
    public static void main(String[] args){
         int NUM_CORES = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_CORES);
        for(int i = 0; i< NUM_CORES; i++){
            executorService.execute(() ->{
                int result = performComputations();
                System.out.println("Completed result" + Thread.currentThread().getName() + " : "+  result);
            });
        }
        executorService.shutdown();
// CPU-intensive tasks → Executors.newFixedThreadPool(n)
//CPU-bound tasks (like image processing, video encoding, or complex calculations) spend most of their time using the CPU, rather than waiting for external resources.
//Too many threads can lead to excessive context switching, slowing down performance.
//A fixed number of threads (equal to the number of CPU cores) ensures that CPU resources are fully utilized without excessive overhead.


    }

    private static int performComputations(){
        int sum = 0;
        for(int i = 0; i< 100000; i++ ){
            sum+= Math.sqrt(i);
        }
        return sum;
    }

}
