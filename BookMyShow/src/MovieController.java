import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieController {
    Map<City, List<Movie>> cityVsMovie ;

    List<Movie> movies;
    MovieController(){
        cityVsMovie = new HashMap<>();
        movies = new ArrayList<>();
    }

    public List<Movie> getMoviesByCity(City city){
        return cityVsMovie.get(city);
    }

    public void addMovie(Movie movie, City city){
        movies.add(movie);
        List<Movie> cityMovies = cityVsMovie.getOrDefault(city, new ArrayList<>());


        cityMovies.add(movie);
        cityVsMovie.put(city, cityMovies);
    }

    public Movie getMovieByName(String movieName){
        for(Movie movie: movies){
            if(movie.getName().equals(movieName))
                return  movie;
        }
        return  null;
    }

    //Remove from a particular city, make use of CityVsMovies map

    //Update movie of a particular city
    //CRUD operation based on Movie Id

}
