package MultiThreading.StampedLock;

import java.util.concurrent.locks.StampedLock;

public class SharedResource {
    StampedLock lock = new StampedLock();
    int a = 10;
    public void produce(){
        long stamp = lock.tryOptimisticRead();
        try{
            System.out.println("Optimistic read lock acquired");
            a = 11;
            Thread.sleep(6000);
            if(lock.validate(stamp)){
                System.out.println("updated a value successfully");
            } else{
            System.out.println("rollbacking work");
              a= 10;
            }

        } catch (Exception e){
            throw  new RuntimeException();
        }
    }

    public void consume(){
        long stamp = lock.writeLock();
        System.out.println("Write lock acquired by" + Thread.currentThread().getName());
        try{
            a = 10;
            System.out.println("Updating the value");

        } finally {
            lock.unlockWrite(stamp);
            System.out.println("Write lock released by " + Thread.currentThread().getName());
        }
    }
}
