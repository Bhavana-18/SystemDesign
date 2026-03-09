package MultiThreading;

public class ProducerConsumer {

    public static void main(String[] args){
        SharedResource sharedResource = new SharedResource();
        Thread producerThread = new Thread(()->{
            try{
                Thread.sleep(2000);
            } catch (Exception e){

            }
            sharedResource.addItem();
        });
        Thread consumerThread = new Thread(sharedResource::consumeItem);
        System.out.println("Starting the threads");
        producerThread.start();
        consumerThread.start();


    }
}
