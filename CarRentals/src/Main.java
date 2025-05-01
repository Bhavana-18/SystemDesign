import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        CarRentalSystem rentalSystem = CarRentalSystem.getInstance();

        User user1 = new User("Bhavana", "bhavana@gmail.com" ," ABCD");
        rentalSystem.addVehicle(new Vehicle("Toyoto", "xyz", VehicleType.CAR,10.0, "abchgt"));
        rentalSystem.addVehicle(new Vehicle("Toyota", "Camry",VehicleType.CAR, 50.0, "ABC123"));
        rentalSystem.addVehicle(new Vehicle("Honda", "Civic", VehicleType.CAR,45.0, "XYZ789"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(3);
        List<Vehicle> availableVehicles = rentalSystem.searchCars("Toyoto", "xyz",startDate,endDate);

        if(!availableVehicles.isEmpty()){
            Vehicle vehicle = availableVehicles.getFirst();
            Reservation reservation = rentalSystem.makeReservation(user1, vehicle, startDate, startDate, endDate);

            if(reservation != null){
                boolean paymentSuccess = rentalSystem.processPayment(reservation);

                if(paymentSuccess){
                    System.out.println("Reservation is successful, reservationId: " + reservation.getReservationId() );
                } else{
                    System.out.println("Payment Failed, please try again!");
                }
            }
        }

    }
}