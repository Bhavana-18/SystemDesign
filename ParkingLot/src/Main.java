public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello, World!");
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.addParkingSpot(new TwoWheelerSpot(1, new Position(1,1)));
        parkingLot.addParkingSpot(new FourWheelerSpot(2, new Position(9,9)));

        Gate gateA = new Gate(1, "North gate", new Position(0,0));
        Gate gateB = new Gate(2, "South Gate", new Position(10,10));

        EntryGateManager entryGateManager = new EntryGateManager(parkingLot,gateB);
        Vehicle bike = new Vehicle("TS0123", VehicleType.TwoWheeler);

       ParkingTicket ticket = entryGateManager.allowEntry(bike);
       Thread.sleep(3600 );
       ExitGateManager exitGateManager = new ExitGateManager(parkingLot, new HourlyBasedPricingStrategy());
       double amountToPay = exitGateManager.processRate(ticket);
       System.out.println("Amount to pay" + amountToPay);
       parkingLot.printAvailableParkingSpots();

    }
}