package MultiThreading;

public class VolatileKeyWord {
   //changes in one thread are immediately visible to others
    private volatile boolean running = true;
    int count = 0;

    public void increment(){
        while(running){
            count++;
            System.out.println("Count:" + count);
        }

    }

    public void stop(){
        running = false;
        System.out.println("stopped");
    }

    public static void main(String[] args){
        VolatileKeyWord example = new VolatileKeyWord();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                    example.increment();


            }
        });
        thread.start();
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
       example.stop();
    }
}

