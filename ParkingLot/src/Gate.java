public class Gate {
    private final int id;
    private final String name;
    private final Position position;

    Gate(int id, String name, Position position){
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
