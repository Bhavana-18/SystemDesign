import java.util.ArrayList;
import java.util.List;

public class Theatre {
    int theatreId;
    String address;
    City city;
    List<Screen> screenList;
    List<Show> shows;

    public Theatre(int id, String address, City city){
        this.theatreId = id;
        this.address = address;
        this.city = city;
        screenList = new ArrayList<>();
        shows = new ArrayList<>();
    }

    public void setScreenList(List<Screen> screenList) {
        this.screenList = screenList;
    }

    public List<Screen> getScreenList() {
        return screenList;
    }

    public void addScreen(Screen screen){
        screenList.add(screen);
    }

    public void addShow(Show show){
        shows.add(show);
    }

    public List<Show> getShows() {
        return shows;
    }
}
