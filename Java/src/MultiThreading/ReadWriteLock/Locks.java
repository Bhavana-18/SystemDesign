package MultiThreading.ReadWriteLock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Locks {
    public static void main(String[] args) {
        SharedResource sharedResource1 = new SharedResource();
        SharedResource sharedResource2 = new SharedResource();
        SharedResource sharedResource3 = new SharedResource();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Thread thread1 = new Thread(() -> {
            sharedResource1.produce(lock);
        });
        Thread thread2 = new Thread(() -> {
            sharedResource2.produce(lock);
        });
        Thread thread3 = new Thread(() -> {
            sharedResource3.consume(lock);
        });
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
