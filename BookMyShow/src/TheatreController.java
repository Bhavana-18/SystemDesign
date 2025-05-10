import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheatreController {
    Map<City, List<Theatre>> cityVsTheatre;
    List<Theatre> allTheatres;

    TheatreController(){
        cityVsTheatre = new HashMap<>();
        allTheatres = new ArrayList<>();
    }

   public void addTheatre(Theatre theatre, City city){
        List<Theatre> theatres = cityVsTheatre.getOrDefault(city, new ArrayList<>());
        theatres.add(theatre);
        cityVsTheatre.put(city, theatres);
   }

   public Map<Theatre, List<Show>> getShows(Movie movie, City city){
        List<Theatre> theatres = cityVsTheatre.getOrDefault(city, new ArrayList<>());
        Map<Theatre, List<Show>> theatreListMap = new HashMap<>();
        for(Theatre theatre: theatres){
            List<Show> shows = theatre.getShows();

                List<Show> showList = new ArrayList<>();
                for(Show show: shows){
                    if(show.getMovie() == movie) {
                        showList.add(show);
                    }

                if(!showList.isEmpty()) {
                    theatreListMap.put(theatre, showList);
                }
            }
        }
        return  theatreListMap;
   }


}
