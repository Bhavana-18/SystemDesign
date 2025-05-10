import java.util.ArrayList;
import java.util.List;

public class Show {
    Movie movie;
    int id;
    Screen screen;
    int startTime;
    List<Integer> bookedSeats = new ArrayList<>();
    Show(int id, Screen screen, Movie movie, int startTime ){
        this.id = id;
        this.screen = screen;
        this.movie = movie;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }

    public List<Integer> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<Integer> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
