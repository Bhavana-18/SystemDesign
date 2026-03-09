package main.java.com.logger.sinks;

public class ConsoleSink implements Sink {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
