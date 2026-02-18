package main.java.com.logger;

public enum LoggingLevel {
    DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);
    private final int level;

    LoggingLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLoggable(LoggingLevel minLevel) {
        return this.level >= minLevel.getLevel();
    }

}
