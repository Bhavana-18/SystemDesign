package MultiThreading;

class WorkerThread implements  Runnable{
    int task;
    WorkerThread(int task){
        this.task = task;
    }
    @Override
    public void run(){
        try{
            while(!Thread.currentThread().isInterrupted()){
                System.out.println("Running thread" + Thread.currentThread().getName());
                // A worker thread checking for updates in a loop should exit gracefully when interrupted instead of ignoring the exception.
                Thread.sleep(2000);
            }

        } catch (InterruptedException e) {
          System.out.println("Thread is interrupted");
        }

    }

}
public class ThreadInterruptionExample {
    public static void main(String[] args){
       Thread thread = new Thread(new WorkerThread(1));
        thread.start();
        thread.interrupt();
    }
}
