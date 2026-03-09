package MultiThreading;

public class SharedResource {
    boolean isItemAvailable = false;
    public synchronized  void addItem(){
        isItemAvailable = true;
        System.out.println("Notifying all threads");
        notifyAll();
    }


    public synchronized void  consumeItem(){
        System.out.println("Consumer thread inside consumeItem method");
        while(!isItemAvailable){
            try{
               System.out.println("Consumer thread waiting for items");
               //releasing all monitor locks
                wait();
            } catch (Exception e){
                // exception handling goes here
            }
        }
        System.out.println("Item consumed");
        isItemAvailable = false;
    }
}
