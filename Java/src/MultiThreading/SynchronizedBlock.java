package MultiThreading;

public class SynchronizedBlock {
    private int count = 0;
    private final Object lock = new Object();
    private  void increment() {
        System.out.println("Inside the synchronized block");
        synchronized (lock){
        count++;
        System.out.println("Synchronized Method - Counter value after increment: " + count);
        System.out.println("Synchronized Method - End increment: " + Thread.currentThread().getName());

    }
        System.out.println("Synchronized Method - End increment: " + Thread.currentThread().getName());}
    private int getCount(){
        return count;
    }
    public static void main(String[] args){
      SynchronizedBlock counter = new SynchronizedBlock();
        int NUM_OF_THREADS = 5;
        Thread[] threads = new Thread[NUM_OF_THREADS];
        for(int i = 0; i< NUM_OF_THREADS; i++){
            try{
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        counter.increment();
                    }
                }, "Thread -" +(i+1));
                threads[i].start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        for(int i = 0; i<NUM_OF_THREADS; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}


//You can also use Atomic Variable for thread safe and lock free 
