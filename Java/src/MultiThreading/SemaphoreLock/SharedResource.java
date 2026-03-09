package MultiThreading.SemaphoreLock;

import java.util.concurrent.Semaphore;

public class SharedResource {
    Semaphore lock = new Semaphore(2); // only 2 threads can acquire lock at the same time

    void produce(){
        try{
            lock.acquire();
            System.out.println("Acquiring lock " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Realeasing lock " + Thread.currentThread().getName());
            lock.release();
        }

    }
}
