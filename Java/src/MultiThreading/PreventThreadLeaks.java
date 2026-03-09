package MultiThreading;
class SafeLock{
    Object lock = new Object();

    public void waitForSignal(){
        synchronized (lock) {
            try {
                System.out.println("Waiting for the lock");
                lock.wait(3000); // timwout so that thread will not wait for longer
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

public class PreventThreadLeaks {
    public static void main (String[] args) {
        SafeLock safeLock = new SafeLock();
        Thread thread = new Thread(
                safeLock::waitForSignal,
                "Worker Thread"
        );

        thread.start();
    }
}
