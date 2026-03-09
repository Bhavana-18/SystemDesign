package MultiThreading.ReentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class SharedResource {
    public void produce(ReentrantLock lock){
        try{
            lock.lock();
            System.out.println("Acquring lock");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            System.out.println("Releasing lock");
        }
    }
}
