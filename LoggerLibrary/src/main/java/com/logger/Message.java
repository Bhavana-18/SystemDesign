package main.java.com.logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

public class Message {
    private final LoggingLevel level;
    private final String content;
    private final String nameSpace;
    private final LocalDateTime timestamp;
    private final String trackingId;
    private final String hostName;

    Message(LoggingLevel level, String content, String nameSpace, String trackingId) {
        this.level = level;
        this.content = content;
        this.nameSpace = nameSpace;
        this.timestamp = LocalDateTime.now();
        this.trackingId = trackingId;
        this.hostName = getHostNameFromServer();

    }

    private static String getHostNameFromServer() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown-host";
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LoggingLevel getLevel() {
        return level;
    }

    public String getContent() {
        return content;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getHostName() {
        return hostName;
    }
}
