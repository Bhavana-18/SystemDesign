import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private  final String reservationId;
    private  final  User user;
    private  final Vehicle vehicle;
    private  final LocalDate bookingDate;
    private  final LocalDate startDate;
    private  final LocalDate endDate;
    private  final double totalPrice;

    Reservation(String reservationId, User user, Vehicle vehicle, LocalDate bookingDate , LocalDate startDate, LocalDate endDate){
        this.reservationId = reservationId;
        this.user = user;
        this.bookingDate = bookingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.vehicle = vehicle;
        this.totalPrice = calculateTotalPrice();
    }

    public  double calculateTotalPrice(){
        long daysRented = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return  daysRented* vehicle.getRentalPricePerDay();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getReservationId() {
        return reservationId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public User getUser() {
        return user;
    }
}
