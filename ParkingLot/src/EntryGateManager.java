public class EntryGateManager {
    private  final ParkingLot parkingLot;
    private final Gate gate;

    EntryGateManager(ParkingLot parkingLot, Gate gate){
        this.parkingLot = parkingLot;
        this.gate = gate;
    }
    public ParkingTicket allowEntry(Vehicle vehicle){
        ParkingSpot parkingSpot = parkingLot.findNearestParkingSpotAvailable(vehicle,gate.getPosition());
        parkingSpot.parkVehicle(vehicle);
        return  new ParkingTicket(parkingSpot.getId(), vehicle, System.currentTimeMillis());
    }

}
