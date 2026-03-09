package main.java.com.logger;

import main.java.com.logger.config.LoggerConfig;
import main.java.com.logger.sinks.AsyncSink;
import main.java.com.logger.sinks.ConsoleSink;
import main.java.com.logger.sinks.FileSink;
import main.java.com.logger.sinks.Sink;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static main.java.com.logger.constants.LoggerConstants.*;

public class LogManager {
    public static volatile LogManager instance;
    private Logger logger;

    private LogManager() {

    }

    public static LogManager getInstance() {
        if (instance == null) {
            synchronized (LogManager.class) {
                instance = new LogManager();
            }
        }
        return instance;
    }

    public void initialize(LoggerConfig loggerConfig) {
        MessageFormatter formatter = new MessageFormatter(loggerConfig.getTimestampFormat());
        Map<LoggingLevel, List<Sink>> sinks = createSink(loggerConfig);
        logger = new Logger(sinks, formatter, loggerConfig.getLogLevel());
    }

    public Map<LoggingLevel, List<Sink>> createSink(LoggerConfig loggerConfig) {
        Sink baseSink;
        switch (loggerConfig.getSinkType()) {
            case fileSink:
                if (loggerConfig.getFileLocation() == null) {
                    throw new IllegalArgumentException("File location must be provided for FILE sink.");
                }
                baseSink = new FileSink(loggerConfig.getFileLocation(), Integer.parseInt(loggerConfig.getFileSize()));
                break;

            case consoleSink:
            default:
                baseSink = new ConsoleSink();
                break;

        }
        Sink finalSink;
        if (asyncConfigType.equals(loggerConfig.getWriteMode())) {
            finalSink = new AsyncSink(baseSink, loggerConfig.getThreadModel());
        } else {
            finalSink = baseSink;
        }

        Map<LoggingLevel, List<Sink>> sinkMap = new ConcurrentHashMap<>();
        for (LoggingLevel level : LoggingLevel.values()) {
            if (level.isLoggable(loggerConfig.getLogLevel())) {
                sinkMap.put(level, Collections.singletonList(finalSink));
            }
        }
        return sinkMap;


    }

    public Logger getLogger() {
        if (logger == null) {
            throw new IllegalStateException("LogManager has not been initialized. Call initialize() first.");
        }
        return logger;

    }

}
