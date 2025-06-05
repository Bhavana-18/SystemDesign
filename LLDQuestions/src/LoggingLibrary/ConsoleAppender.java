package LoggingLibrary;

public class ConsoleAppender implements  Appender {

    @Override
    public void append(LoggingLevel level, String  message){
        System.out.println(level + ":" + message);
    }
}
