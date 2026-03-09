package MultiThreading.ProducerConsumerSol;

import java.util.LinkedList;
import java.util.Queue;

public class SharedResource {
    Queue<Integer> sharedBuffer;
    int bufferSize;
    public SharedResource(int bufferSize){
        sharedBuffer = new LinkedList<>();
        this.bufferSize = bufferSize;

    }

    public synchronized void produce(int item) {
        while(sharedBuffer.size() == bufferSize){
            try{
            System.out.println("Shared Resource is full");
            wait();}
            catch (Exception e){

            }
        }
        sharedBuffer.add(item);
        System.out.println("produced item:"+  item);
        notify();
    }
    public synchronized int consume(){
        while(sharedBuffer.isEmpty()){
            try {
                System.out.println("Shared resource is empty");
                wait();
            } catch (InterruptedException e){
                // do something
            }
        }
        int item = sharedBuffer.poll();
        System.out.println("consumed item:"+  item);
        notify();
        return  item;
    }
}
