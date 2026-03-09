package MultiThreading;

public class ThreadJoin {
    public static void main(String[] args){
        SharedResources sharedResource = new SharedResources();
        Thread thread = new Thread(() ->{
            System.out.println("Thread 1 starting");
            sharedResource.produce();
        });
        thread.setPriority(1); //sets the priority of thread
        thread.setDaemon(true);
        thread.start();



        try{
            System.out.println("Main thread is waiting for thread1 to finish now");
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main thread is finishing its work");


    }
}
