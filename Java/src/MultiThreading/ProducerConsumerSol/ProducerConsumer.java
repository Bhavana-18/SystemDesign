package MultiThreading.ProducerConsumerSol;

public class ProducerConsumer {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource(3);

        Thread producerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    sharedResource.produce(i);
                }
            } catch (Exception e) {

            }
        });
        Thread consumerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    sharedResource.consume();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        producerThread.start();
        consumerThread.start();
    }


}
