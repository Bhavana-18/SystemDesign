public class Vehicle {
    private final String licensePlate;
    private final VehicleType vehicleType;

    Vehicle(String licensePlate, VehicleType vehicleType){
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate(){
        return  this.licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
