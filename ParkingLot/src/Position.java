public class Position {
    private final int x;
    private final int y;

    public Position(int x , int y){
        this.x = x;
        this.y = y;

    }

    public double distanceTo(Position other){
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);

    }

}
