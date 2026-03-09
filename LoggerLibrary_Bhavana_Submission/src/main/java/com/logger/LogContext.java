package main.java.com.logger;


public class LogContext {

    private static final ThreadLocal<String> trackingId = new ThreadLocal<>();

    public static String getTrackingId() {
        return trackingId.get();
    }

    public static void setTrackingId(String id) {
        if (id == null) {
            trackingId.remove();
        } else {
            trackingId.set(id);
        }
    }

    public static void clear() {
        trackingId.remove();
    }
}


