package main.java.com.logger;

import main.java.com.logger.sinks.Sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logger {
    private final Map<LoggingLevel, List<Sink>> levelVsSinks;
    MessageFormatter messageFormatter;
    LoggingLevel minLoggingLevel;


    public Logger(Map<LoggingLevel, List<Sink>> sinks, MessageFormatter messageFormatter, LoggingLevel minLevel) {
        this.levelVsSinks = sinks != null ? new ConcurrentHashMap<>(sinks) : new ConcurrentHashMap<>();
        this.messageFormatter = messageFormatter;
        this.minLoggingLevel = minLevel;
    }

    public void log(LoggingLevel level, String content, String namespace) {

        if (level.isLoggable(minLoggingLevel)) {
            String trackingId = LogContext.getTrackingId();
            Message message = new Message(level, content, namespace, trackingId);
            String formattedMessage = messageFormatter.format(message);
            List<Sink> sinkList = levelVsSinks.getOrDefault(level, new ArrayList<>());
            for (Sink sink : sinkList) {
                sink.log(formattedMessage);
            }
        }
    }

    public void debug(String content, String nameSpace) {
        log(LoggingLevel.DEBUG, content, nameSpace);
    }

    public void info(String content, String nameSpace) {
        log(LoggingLevel.INFO, content, nameSpace);

    }

    public void error(String content, String nameSpace) {
        log(LoggingLevel.ERROR, content, nameSpace);
    }

    public void fatal(String content, String nameSpace) {
        log(LoggingLevel.FATAL, content, nameSpace);
    }

    public void warn(String content, String nameSpace) {
        log(LoggingLevel.WARN, content, nameSpace);
    }
}
