package main.java.com.logger;

import main.java.com.logger.config.LoggerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        LogManager logManager = LogManager.getInstance();
        System.out.println("--- Running Demo 1: Synchronous File Logging ---");
        Map<String, String> fileConfigMap = new HashMap<>();
        fileConfigMap.put("ts_format", "dd-MM-yyyy HH:mm:ss");
        fileConfigMap.put("log_level", "DEBUG");
        fileConfigMap.put("sink_type", "FILE");
        fileConfigMap.put("file_location", "logs/application.log");
        fileConfigMap.put("write_mode", "SYNC");
        fileConfigMap.put("file_size", "512");
        String request1Id = UUID.randomUUID().toString();

        LogContext.setTrackingId(request1Id);

        LoggerConfig fileConfig = new LoggerConfig(fileConfigMap);
        LogManager.getInstance().initialize(fileConfig);

        Logger fileLogger = LogManager.getInstance().getLogger();

        try {
            fileLogger.info("Application starting up...", "phonePe.payments");
            fileLogger.debug("Reading configuration files.", "phonePe.payments");
            fileLogger.warn("Connection pool is almost full.", "phonePe.payments");
            fileLogger.error("User authentication failed for user 'admin'.", "phonePe.payments");
            fileLogger.fatal("Cannot connect to the database, shutting down", "phonePe.payments");
        } finally {
            LogContext.clear();
        }

        System.out.println("Demo 1 finished. Check 'logs/application.log'.");
        System.out.println();


        System.out.println("--- Running Demo 2: Asynchronous Console Logging ---");
        Map<String, String> consoleConfigMap = new HashMap<>();

        consoleConfigMap.put("ts_format", "yyyy/MM/dd HH:ss");
        consoleConfigMap.put("log_level", "WARN");
        consoleConfigMap.put("sink_type", "CONSOLE");
        consoleConfigMap.put("write_mode", "ASYNC");

        LoggerConfig consoleConfig = new LoggerConfig(consoleConfigMap);
        LogManager.getInstance().initialize(consoleConfig);
        String request2Id = UUID.randomUUID().toString();
        LogContext.setTrackingId(request2Id);
        Logger consoleLogger = LogManager.getInstance().getLogger();
        try {
            consoleLogger.info("This INFO message will NOT be logged.", "phonePe.merchants");
            consoleLogger.warn("This is a warning.", "phonePe.merchants");
            consoleLogger.error("This is an error.", "phonePe.merchants");
        } finally {
            LogContext.clear();
        }

        Thread.sleep(100);
        System.out.println("Demo 2 finished.");

    }
}