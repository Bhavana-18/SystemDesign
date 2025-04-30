public class Ladder {
    private final int id;
    private final int start;
    private final int end;

    Ladder(int id, int start, int end){
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
