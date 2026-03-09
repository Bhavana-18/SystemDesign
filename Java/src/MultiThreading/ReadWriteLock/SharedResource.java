package MultiThreading.ReadWriteLock;

import java.util.concurrent.locks.ReadWriteLock;

public class SharedResource {
    boolean isAvailable;
    SharedResource(){
        isAvailable = true;
    }

    public void produce(ReadWriteLock lock){
        try{
            lock.readLock().lock();
            System.out.println("Read lock acquired by thread" + Thread.currentThread().getName());
            Thread.sleep(8000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
            System.out.println("Read lock released by thread" + Thread.currentThread().getName());
        }
    }

    public void consume(ReadWriteLock lock){
        try{
            lock.writeLock().lock();
            System.out.println("Write lock acquired by thread" + Thread.currentThread().getName());
            isAvailable = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
            System.out.println("Write lock released by thread" + Thread.currentThread().getName());
        }
    }
}
