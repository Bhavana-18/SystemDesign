public class BookingService {
    public Booking createBooking(User user, Show show, List<Seat> requestedSeats, PaymentStrategy paymentStrategy) {
        // 1. Attempt to lock/reserve seats
        boolean success = show.bookSeats(requestedSeats);

        if (success) {
            double amount = requestedSeats.size() * show.getTicketPrice();
            if (paymentStrategy.processPayment(amount)) {
                return new Booking(user, show, requestedSeats);
                
            } else {
                // Rollback: if payment fails, free the seats
                show.releaseSeats(requestedSeats);
            }
        }
        throw new RuntimeException("Booking Failed: Seats unavailable");
    }
}