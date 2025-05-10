import java.util.ArrayList;
import java.util.List;

public class Screen {
    private int screenId;
    private List<Seat> seats;

    Screen(int screenId){
      this.screenId = screenId;
        seats = new ArrayList<>();
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }


    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
