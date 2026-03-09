import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello, World!");
        int numberOfElevators = 2;

        List<Elevator> elevators = new ArrayList<>();

        // Create elevators
        for (int i = 0; i < numberOfElevators; i++) {
            Elevator elevator = new Elevator(i,0);
            elevators.add(elevator);
            new Thread(elevator, "Elevator-" + i).start();
        }

        // Create controller
        ElevatorController controller = new ElevatorController(elevators, new DirectionAwareStrategy());

        // Simulate hall requests
        controller.requestElevator(new HallRequest(3, Direction.UP));
        Thread.sleep(2000);

        controller.requestElevator(new HallRequest(7, Direction.DOWN));
        Thread.sleep(2000);

        controller.requestElevator(new HallRequest(1, Direction.UP));
        Thread.sleep(2000);

        controller.requestElevator(new HallRequest(9, Direction.DOWN));

    }
}