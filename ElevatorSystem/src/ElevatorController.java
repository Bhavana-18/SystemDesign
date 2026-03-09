import java.util.List;

public class ElevatorController {
    private final List<Elevator> elevatorList;
    private final ElevatorSelectionStrategy selectionStrategy;
    public  ElevatorController (List<Elevator> elevators, ElevatorSelectionStrategy elevatorSelectionStrategy){
        this.elevatorList = elevators;
        this.selectionStrategy = elevatorSelectionStrategy;
    }
    public void requestElevator(HallRequest request){
        Elevator best = selectionStrategy.select(request,elevatorList);
        System.out.println("Assigning florr" + request.getFloor() + "to Elevator" + best.getId());
        best.submitRequest(new CarRequest(request.getFloor()));
    }
}
