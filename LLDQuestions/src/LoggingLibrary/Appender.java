package LoggingLibrary;

public interface Appender {
    public void append(LoggingLevel level, String message);
}
