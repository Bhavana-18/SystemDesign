import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarRentalSystem {
   private Map<String, Vehicle> cars;
   private Map<String , Reservation> reservationMap;
   private PaymentProcessor paymentProcessor;
   private static CarRentalSystem instance;

   CarRentalSystem(){
       cars = new ConcurrentHashMap<>();
       reservationMap = new ConcurrentHashMap<>();
       paymentProcessor = new PaymentProcessor();
   }

    public static synchronized  CarRentalSystem getInstance() {
       if(instance == null)
           instance = new CarRentalSystem();
        return instance;
    }

    public  void addVehicle(Vehicle vehicle){
       cars.put(vehicle.getLicensePlate(), vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
       cars.remove(vehicle.getLicensePlate());
    }

    public List<Vehicle>  searchCars(String make, String model, LocalDate startDate, LocalDate endDate){
       List<Vehicle> vehicles = new ArrayList<>();
       for(Vehicle vehicle: cars.values()){
           if(vehicle.isAvailable() && vehicle.getMake().equals(make) && vehicle.getModel().equals(model)){
               if(isVehicleAvailable(vehicle,startDate, endDate))
                   vehicles.add(vehicle);
           }
       }
    return vehicles;
    }

    public boolean isVehicleAvailable(Vehicle vehicle, LocalDate startDate, LocalDate endDate){
       for(Reservation reservation : reservationMap.values()){
           if(reservation.getVehicle() == vehicle){
               if(startDate.isBefore(reservation.getEndDate()) && endDate.isAfter(reservation.getStartDate()))
                   return  false;

           }
       }
       return  true;
    }

    public synchronized Reservation makeReservation(User user,Vehicle vehicle,LocalDate bookingDate, LocalDate startDate, LocalDate endDate){
       if(isVehicleAvailable(vehicle,startDate, endDate)){
           String reservationId = generateId();
           Reservation reservation = new Reservation(reservationId, user, vehicle,bookingDate,startDate, endDate);
           vehicle.setAvailable(false);
           return reservation;
       }
       return null;
    }

    public synchronized void cancelReservation(Reservation res){
      Reservation reservation=  reservationMap.remove(res.getReservationId());
      if(reservation != null)
       reservation.getVehicle().setAvailable(true);
    }

    public boolean processPayment(Reservation reservation){
       return  paymentProcessor.processPayment(reservation.getTotalPrice());
    }

    private String generateId(){
       return "RES"+ UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
}
