import java.security.PublicKey;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerExample {
    private static final BlockingQueue<Integer>  queue= new ArrayBlockingQueue<>(5);

    public static void main(String[] args){
        Runnable producer = () ->{
            try{
                for(int i = 0; i<10; i++){
                    queue.put(i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        };

        Runnable consumer = (() -> {
            try{
                while(true){
                    queue.take();
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        });

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
