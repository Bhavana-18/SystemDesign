package main.java.com.logger.sinks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static main.java.com.logger.constants.LoggerConstants.multiThreading;

public class AsyncSink implements Sink {
    private final Sink baseSink;
    private final BlockingQueue<String> messageQueue;
    private final ExecutorService executorService;

    public AsyncSink(Sink baseSink, String threadModel) {
        this.baseSink = baseSink;
        this.messageQueue = new LinkedBlockingDeque<>();
        if (multiThreading.equalsIgnoreCase(threadModel)) {
            int coreCount = Runtime.getRuntime().availableProcessors();
            this.executorService = Executors.newFixedThreadPool(coreCount > 1 ? coreCount : 2);
            System.out.println("initialized with multi thread model.");

        } else {
            this.executorService = Executors.newSingleThreadExecutor();
            System.out.println("initialized with single thread");
        }
        startConsumer();
    }

    private void startConsumer() {
        Runnable consumerTask = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String message = messageQueue.take();
                    baseSink.log(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
        executorService.submit(consumerTask);
    }

    @Override
    public void log(String message) {
        try {
            messageQueue.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        executorService.shutdown();
    }


}
