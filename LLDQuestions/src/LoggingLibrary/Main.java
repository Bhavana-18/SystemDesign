package LoggingLibrary;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Hello, World!");
        Logger logger = new Logger(LoggingLevel.INFO);
        logger.addAppender(new ConsoleAppender());
        //logger.addAppender(new FileAppender("/root"));
        logger.debug("Doing debug");
    }
}
