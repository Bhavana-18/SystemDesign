import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParkingLot {
    private final List<ParkingSpot> parkingSpots = new ArrayList<>();
    public void addParkingSpot(ParkingSpot parkingSpot){
        parkingSpots.add(parkingSpot);
    }

    public ParkingSpot findNearestParkingSpotAvailable(Vehicle vehicle , Position gatePosition){

        return parkingSpots.stream().filter(spot -> spot.isAvailable() && spot.vehicleType == vehicle.getVehicleType())
                .min(Comparator.comparingDouble(spot -> spot.getPosition().distanceTo(gatePosition)))
                .orElseThrow(() -> new RuntimeException("No available spot"));

    }

    void removeVehicle(int spotId){
        for(ParkingSpot parkingSpot :parkingSpots){
            if(parkingSpot.getId() == spotId){
                parkingSpot.removeVehicle();
                return;
            }
        }
    }

    void printAvailableParkingSpots(){
        System.out.println("Available parking spots:");
        for(ParkingSpot parkingSpot : parkingSpots){
            if(parkingSpot.isAvailable()){
                System.out.println("Spot Id" + parkingSpot.getId() + " Type: " + parkingSpot.vehicleType);

            }
        }

    }

}
