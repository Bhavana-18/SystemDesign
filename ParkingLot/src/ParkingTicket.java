import java.sql.Timestamp;

public class ParkingTicket {

    private final int spotId;
    private final Vehicle vehicle;
    private final long timestamp;

    ParkingTicket(int spotId , Vehicle vehicle, long timestamp){
        this.spotId = spotId;
        this.vehicle = vehicle;
        this.timestamp = timestamp;
    }

    public int getSpotId() {
        return spotId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
