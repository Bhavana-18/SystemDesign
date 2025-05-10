public class Movie {

    private final int id;
    private final String name;
    private final int duration;
    private final String description;

    Movie(int id, String name, int duration, String description){
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }
}
