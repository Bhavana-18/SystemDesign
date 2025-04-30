public class Request {
    private final int startFloor;
    private final int destinationFloor;

    Request(int startFloor, int destinationFloor){
        this.startFloor = startFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }
    public int getStartFloor(){
        return  startFloor;
    }
}
