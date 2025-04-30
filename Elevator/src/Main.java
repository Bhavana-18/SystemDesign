public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        ElevatorController elevatorController = new ElevatorController(3, 5);
        elevatorController.getElevator(new Request(1,3));
        elevatorController.getElevator(new Request(4, 2));
    }
}