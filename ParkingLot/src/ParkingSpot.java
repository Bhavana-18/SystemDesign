
public class ParkingSpot {

    protected  Vehicle vehicle;
    private final VehicleType vehicleType;
    protected final int id;
    private final Position position;

    public ParkingSpot(VehicleType vehicleType, int id, Position position){
        this.vehicleType = vehicleType;
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public Vehicle getVehicle(){
        return this.vehicle;
    }

    public  synchronized  boolean isAvailable(){
        return this.vehicle == null;
    }

    public synchronized void parkVehicle(Vehicle vehicle){
        if(isAvailable() && vehicle.getVehicleType() == vehicleType){
            this.vehicle = vehicle;
        }else {
            throw new IllegalArgumentException("Invalid vehicle type or spot already occupied.");
        }
    }
    public synchronized void removeVehicle(){
        this.vehicle = null;
    }

    public Position getPosition() {
        return position;
    }
}
