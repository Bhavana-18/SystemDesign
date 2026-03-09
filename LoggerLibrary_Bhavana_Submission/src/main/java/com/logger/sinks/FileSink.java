package main.java.com.logger.sinks;

import java.io.*;
import java.util.zip.GZIPOutputStream;

import static main.java.com.logger.constants.LoggerConstants.defaultMaxFileSize;
import static main.java.com.logger.constants.LoggerConstants.maxRotationIndex;

public class FileSink implements Sink {
    private final String filePath;
    private final long maxFileSize;
    PrintWriter writer;
    private File logFile;

    public FileSink(String filePath) {
        this.filePath = filePath;
        this.maxFileSize = defaultMaxFileSize;
        logFile = new File(filePath);
        logFile.getParentFile().mkdirs();
        initializeWriter();
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));

    }

    public FileSink(String filePath, long maxSize) {
        this.filePath = filePath;
        this.maxFileSize = maxSize;
        logFile = new File(filePath);
        logFile.getParentFile().mkdirs();
        initializeWriter();
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    private void initializeWriter() {
        try {
            this.writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
        } catch (IOException e) {
            System.err.println("Error: Could not open log file for writing: " + e.getMessage());
            this.writer = null;
        }
    }

    @Override
    public synchronized void log(String message) {
        if (writer == null) {
            System.err.println("logger not initialized correctly. Cannot write to file.");
            return;
        }

        checkAndRotate();
        writer.println(message);
        writer.flush();

    }


    private void compressFile(File sourceFile, File targetFile) throws IOException {

        try (GZIPOutputStream gos = new GZIPOutputStream(
                new FileOutputStream(targetFile));

             FileInputStream fis = new FileInputStream(sourceFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                gos.write(buffer, 0, len);
            }
        }
    }

    private void checkAndRotate() {
        if (logFile.length() <= maxFileSize) {
            return;
        }

        System.out.println("Log file size exceeds limit. Starting rotation...");

        close();

        try {
            String basePath = filePath;
            String extension = "";
            int dotIndex = filePath.lastIndexOf('.');
            if (dotIndex > 0) {
                basePath = filePath.substring(0, dotIndex);
                extension = filePath.substring(dotIndex);
            }

            File oldestFile = new File(basePath + "." + maxRotationIndex + extension + ".gz");
            if (oldestFile.exists() && !oldestFile.delete()) {
                System.err.println("Could not delete oldest log file: " + oldestFile);
            }

            for (int i = maxRotationIndex - 1; i >= 1; i--) {
                File current = new File(basePath + "." + i + extension + ".gz");
                if (current.exists()) {
                    File next = new File(basePath + "." + (i + 1) + extension + ".gz");
                    if (!current.renameTo(next)) {
                        System.err.println("Could not rename " + current + " to " + next);
                    }
                }
            }

            File newCompressedFile = new File(basePath + ".1" + extension + ".gz");
            compressFile(this.logFile, newCompressedFile);

            // Delete the original active log file
            if (!this.logFile.delete()) {
                System.err.println("Could not delete active log file after compression.");
            }

        } catch (Exception e) {
            System.err.println("Error during log rotation: " + e.getMessage());

        }
        this.logFile = new File(filePath);
        initializeWriter();
    }


    public synchronized void close() {
        if (this.writer != null) {
            this.writer.close();
            this.writer = null;
        }
    }
}
