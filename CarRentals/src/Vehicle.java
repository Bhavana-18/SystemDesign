public class Vehicle {
    private final String make;
    private  final String model;
    private VehicleType vehicleType;
    private final double rentalPricePerDay;
    private boolean isAvailable;
    private final String licensePlate;

    Vehicle(String make, String model, VehicleType vehicleType, double rentalPricePerDay , String licensePlate){
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.rentalPricePerDay = rentalPricePerDay;
        isAvailable = true;
        this.licensePlate = licensePlate;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}
