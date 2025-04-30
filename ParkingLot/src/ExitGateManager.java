public class ExitGateManager {
private final ParkingLot parkingLot;
private final PricingStrategy pricingStrategy;

ExitGateManager (ParkingLot parkingLot , PricingStrategy pricingStrategy){
    this.parkingLot = parkingLot;
    this.pricingStrategy = pricingStrategy;
}

public double processRate(ParkingTicket parkingTicket){
    long duration = System.currentTimeMillis() - parkingTicket.getTimestamp();
    parkingLot.removeVehicle(parkingTicket.getSpotId());
    return pricingStrategy.calculatePrice(duration,parkingTicket.getVehicle().getVehicleType());
}
}
