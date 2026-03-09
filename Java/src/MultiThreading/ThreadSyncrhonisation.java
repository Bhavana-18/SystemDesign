package MultiThreading;

public class ThreadSyncrhonisation {
 private int count = 0;
    private synchronized void increment() {
        System.out.println("Inside the synchronized block");
        count++;
        System.out.println("Synchronized Method - Counter value after increment: " + count);
        System.out.println("Synchronized Method - End increment: " + Thread.currentThread().getName());
    }
    private int getCount(){
        return count;
    }
    public static void main(String[] args){
        ThreadSyncrhonisation counter = new ThreadSyncrhonisation();
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
