package MultiThreading;

public class DaemonThread {

    public static void main(String[] args){
        SharedResources sharedResource = new SharedResources();
        Thread thread = new Thread(() ->{
            System.out.println("Thread 1 starting");
            sharedResource.produce();
        });
        thread.setPriority(1); //sets the priority of thread
        thread.setDaemon(true); // it is alive till any one user thread is alive
        thread.start();


        System.out.println("Main thread is finishing its work");


    }
}
