import java.util.List;

public class Booking {
    Show show;
    List<Seat> seats;
    Payment payment;

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
