public class UpiPaymentStrategy implements PaymentStrategy {
    public boolean processPayment(double amount) {
        System.out.println("Processing UPI payment of " + amount);
        return true;
    }
}