public class ElevatorSnapshot {
    final int currentFloor;
    final Direction direction;
    final ElevatorState state;

    ElevatorSnapshot(int currentFloor, Direction direction, ElevatorState state) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.state = state;
    }
}
