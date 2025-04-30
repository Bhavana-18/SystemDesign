import java.util.ArrayList;
import java.util.List;

public class ElevatorController {

    List<Elevator> elevators;
    ElevatorController(int totalElevators, int capacity){
        elevators = new ArrayList<>();
        for(int i = 0; i< totalElevators; i++){
            Elevator elevator = new Elevator(i + 1, capacity);
            elevators.add(elevator);
            new Thread(elevator::run).start();
        }
    }

    public void getElevator(Request request){
        Elevator elevator = findOptimalElevator(request);
        elevator.addRequest(request);

    }

    private  Elevator findOptimalElevator(Request request){
       return elevators.getFirst();
    }
}
