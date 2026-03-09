package MultiThreading;

public class ThreadSuspendAndResume {


    public static void main(String[] args){
        SharedResources sharedResource = new SharedResources();
        Thread thread1 = new Thread(() ->{
            System.out.println("Thread 1 starting");
            sharedResource.produce();
        });

        Thread thread2 = new Thread(() -> {
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){

            }
            sharedResource.produce();
        });
        thread1.start();
        thread2.start();
        System.out.println("Thread 1 is suspended");
        thread1.suspend(); //deprecated methods
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread1.resume(); //DEPECRATED METHODS
        System.out.println("Main thread is finishing its work");


    }


}
