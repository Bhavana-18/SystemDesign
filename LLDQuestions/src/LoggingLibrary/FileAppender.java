package LoggingLibrary;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAppender implements  Appender{
    BufferedWriter writer ;
    FileAppender(String filePath) throws IOException{
        writer = new BufferedWriter(new FileWriter(filePath,true));

    }
    @Override
    public synchronized void append(LoggingLevel level, String message) {
       try{
           writer.write(level + ":" + message + "\n");
           writer.flush();

       } catch (IOException e){
           e.printStackTrace();
       }
    }


}
