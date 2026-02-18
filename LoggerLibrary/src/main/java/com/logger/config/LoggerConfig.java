package main.java.com.logger.config;

import main.java.com.logger.LoggingLevel;

import java.util.Map;

import static main.java.com.logger.constants.LoggerConstants.defaultMaxFileSize;

public class LoggerConfig {
    private final Map<String, String> configMap;

    public LoggerConfig(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public String getTimestampFormat() {
        return configMap.getOrDefault("ts_format", "yyyy-MM-dd HH:mm:ss");
    }

    public LoggingLevel getLogLevel() {
        return LoggingLevel.valueOf(configMap.getOrDefault("log_level", "INFO").toUpperCase());
    }

    public String getSinkType() {
        return configMap.getOrDefault("sink_type", "CONSOLE").toUpperCase();
    }

    public String getFileLocation() {
        return configMap.get("file_location");
    }

    public String getFileSize() {
        return configMap.getOrDefault("file_size", String.valueOf(defaultMaxFileSize));
    }

    public String getWriteMode() {
        return configMap.getOrDefault("write_mode", "SYNC").toUpperCase();
    }

    public String getThreadModel() {
        return configMap.getOrDefault("thread_model", "SINGLE").toUpperCase();
    }

}
