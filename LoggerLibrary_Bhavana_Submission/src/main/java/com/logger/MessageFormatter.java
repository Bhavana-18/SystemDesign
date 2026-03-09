package main.java.com.logger;

import java.time.format.DateTimeFormatter;

public class MessageFormatter {
    DateTimeFormatter dateTimeFormatter;

    MessageFormatter(String tsFormat) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(tsFormat);

    }

    public String format(Message message) {
        String timestamp = message.getTimestamp().format(dateTimeFormatter);
        String trackingId = message.getTrackingId() != null ? message.getTrackingId() : "-";

        return String.format("%s [%s] [%s] [%s] [%s] %s",
                message.getLevel(),
                timestamp,
                message.getHostName(),
                trackingId,
                message.getNameSpace(),
                message.getContent());
    }
}
