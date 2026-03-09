import java.util.List;

public interface ElevatorSelectionStrategy {
    Elevator select(HallRequest request, List<Elevator> elevatorList);
}
