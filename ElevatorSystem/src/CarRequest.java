public class CarRequest implements  Request {
    private final int floor;

    public CarRequest(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }
}
