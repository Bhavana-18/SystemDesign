import java.util.List;

public class DirectionAwareStrategy implements  ElevatorSelectionStrategy {
    @Override
    public Elevator select(HallRequest request, List<Elevator> elevators){
        Elevator best = null;
        int minDistance = Integer.MAX_VALUE;
        for(Elevator elevator : elevators){
            ElevatorSnapshot snap = elevator.getSnapshot();
            int distance = Math.abs(request.getFloor() - snap.currentFloor);

            if(snap.direction == request.getDirection() ){
                if((snap.direction == Direction.UP && request.getFloor() >= snap.currentFloor) || (snap.direction == Direction.DOWN && request.getFloor() <= snap.currentFloor)){
                    if(distance < minDistance){
                        minDistance = distance;
                        best = elevator;
                    }
                }
            }

        }
        if(best != null) return  best;

        for(Elevator elevator : elevators){
            if(elevator.getSnapshot().direction == Direction.IDLE)
                return elevator;
        }
        for (Elevator elevator : elevators) {
            int distance = Math.abs(
                    elevator.getSnapshot().currentFloor - request.getFloor());
            if (distance < minDistance) {
                minDistance = distance;
                best = elevator;
            }
        }

        return best;
    }
}
