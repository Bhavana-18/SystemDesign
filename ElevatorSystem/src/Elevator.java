import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Elevator implements  Runnable {
    private final int id;
    private final Set<Integer> upStops = new TreeSet<>();
    private final Set<Integer> downStops = new TreeSet<>((a, b) ->(b-a));
    private volatile  int currentFloor;
    private volatile  Direction direction;
    private volatile ElevatorState state;
    private volatile boolean running = true;

    private final BlockingDeque<Request> inbox = new LinkedBlockingDeque<>();

    public Elevator(int id, int startFloor){
        this.id = id;
        this.currentFloor = startFloor;
        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;
    }
    public int getId(){
        return id;
    }

    public ElevatorSnapshot getSnapshot(){
        return  new ElevatorSnapshot(currentFloor, direction,state);
    }
    public void submitRequest(Request request){
        inbox.offer(request);
    }
    public void shutdown(){
        running = false;
    }

    @Override
    public void run(){
        try{
            while(running){
                if(upStops.isEmpty() && downStops.isEmpty()){
                    state = ElevatorState.IDLE;
                    waitForRequest();
                }
                state = ElevatorState.MOVING;
                if(direction == Direction.UP){
                    processUp();
                } else{
                    processDown();
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private void waitForRequest() throws InterruptedException{
        Request request = inbox.take();
        addStop(request);
    }

    private void addStop(Request request) {
        int floor = request.getFloor();
        if (floor > currentFloor) {
            upStops.add(floor);
        } else if (floor < currentFloor) {
            downStops.add(floor);
        } else {
            openDoors();
        }
    }
    private void processUp() {
        drainInboxNonBlocking();

        if (!upStops.isEmpty()) {
            int nextStop = ((TreeSet<Integer>) upStops).first();

            moveOneStepUp();

            if (currentFloor == nextStop) {
                upStops.remove(nextStop);
                openDoors();
            }

        } else if (!downStops.isEmpty()) {
            direction = Direction.DOWN; // reverse only when needed
        }
    }

    private void processDown() {
        drainInboxNonBlocking();

        if (!downStops.isEmpty()) {
            int nextStop = ((TreeSet<Integer>) downStops).first();

            moveOneStepDown();

            if (currentFloor == nextStop) {
                downStops.remove(nextStop);
                openDoors();
            }

        } else if (!upStops.isEmpty()) {
            direction = Direction.UP; // reverse only when needed
        }
    }
    private void drainInboxNonBlocking() {
        Request request;
        while ((request = inbox.poll()) != null) {
            addStop(request);
        }
    }

    private void moveOneStepUp() {
        currentFloor++;
        System.out.println("Elevator " + id + " moving UP to floor " + currentFloor);
    }

    private void moveOneStepDown() {
        currentFloor--;
        System.out.println("Elevator " + id + " moving DOWN to floor " + currentFloor);
    }
    private void openDoors() {
        System.out.println("Elevator " + id + " opening doors at floor " + currentFloor);
    }

}
