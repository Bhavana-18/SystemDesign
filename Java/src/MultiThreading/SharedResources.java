package MultiThreading;

public class SharedResources{
    boolean isAvailable;
    public SharedResources(){
        isAvailable = false;
    }
    public synchronized  void produce(){
        System.out.println("Lock acquired");
        try{
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        isAvailable = true;
        System.out.println("Lock released");
    }
}
