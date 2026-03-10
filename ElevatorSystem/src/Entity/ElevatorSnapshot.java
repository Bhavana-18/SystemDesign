package Entity;

import Enum.Direction;
import Enum.ElevatorState;
public class ElevatorSnapshot {
    public final int currentFloor;
    public final Direction direction;
    final ElevatorState state;

    public ElevatorSnapshot(int currentFloor, Direction direction, ElevatorState state) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.state = state;
    }
}
