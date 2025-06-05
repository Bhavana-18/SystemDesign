package LoggingLibrary;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    List<Appender> appenders;
    LoggingLevel currentLevel;

    Logger(LoggingLevel currentLevel){
        appenders = new ArrayList<>();
        this.currentLevel = currentLevel;
    }

    public List<Appender> getAppenders() {
        return appenders;
    }

    public synchronized void addAppender(Appender appender){
        appenders.add(appender);
    }

    public LoggingLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(LoggingLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void log(LoggingLevel level, String message){
        if(currentLevel.ordinal() <= level.ordinal()){
            synchronized (this){
                for(Appender appender : appenders){
                    appender.append(level, message);
                }
            }
        }
    }

    public void info(String  message){
        log(LoggingLevel.INFO , message);
    }

    public void error(String  message){
        log(LoggingLevel.ERROR , message);
    }

    public void debug(String  message){
        log(LoggingLevel.DEBUG, message);
    }

}
