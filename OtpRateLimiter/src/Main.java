import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        OtpRateLimiter limiter = new OtpRateLimiter(
                5,
                Duration.ofMinutes(10)
        );

        boolean allowed = limiter.allowRequest("user123");
        System.out.println("OTP request " + allowed);

        System.out.println("Hello, World!");
    }
}