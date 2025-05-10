import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookMyShow {

    MovieController movieController;
    TheatreController theatreController;

    BookMyShow(){
        movieController = new MovieController();
        theatreController = new TheatreController();
        initialize();
    }

    private void initialize(){
        createMovies();
        createTheatres();
    }

    public void createBooking(City userCity, String movieName){
        List<Movie> movies = movieController.getMoviesByCity(userCity);

        Movie interestedMovie = null;

        for(Movie movie : movies){
            System.out.println(movie);
            if(movie.getName().equals(movieName) )
                interestedMovie = movie;
        }
        Map<Theatre,List<Show>> shows = theatreController.getShows(interestedMovie, userCity);
        Map.Entry<Theatre,List<Show>> entry = shows.entrySet().iterator().next();
        List<Show> runningShows = entry.getValue();
        Show interestedShow = runningShows.get(0);

        List<Integer> bookingSeats = interestedShow.getBookedSeats();
        //Get the seat number;
        int seatNumber = 30;
        if(!bookingSeats.contains(seatNumber)){
            bookingSeats.add(seatNumber);
            Booking booking = new Booking();
            List<Seat> bookedSeats = new ArrayList<>();
            for(Seat seat: interestedShow.getScreen().getSeats()){
                if(seat.getSeatNo() == seatNumber){
                    bookedSeats.add(seat);
                }
            }
            booking.setSeats(bookedSeats);
            interestedShow.setBookedSeats(bookingSeats);
            System.out.println("Booking Successful");
        } else{
            System.out.println("seat already booked");
            return;
        }

    }

    private void createMovies(){
        Movie movie1 = new Movie(1,"Baahubali", 250, "Starring Prabhas");
        Movie movie2 = new Movie(2, "Avengers", 250, "Hollywood movie");
        movieController.addMovie(movie1, City.Bengaluru);
        movieController.addMovie(movie1, City.Chennai);
        movieController.addMovie(movie1, City.Hyderabad);
        movieController.addMovie(movie2, City.Chennai);
        movieController.addMovie(movie2, City.Hyderabad);
        movieController.addMovie(movie2, City.Bengaluru);

    }
    private List<Seat> createSeats(){
        List<Seat> seats = new ArrayList<>();
        for(int i = 0; i<40 ; i++){
            seats.add(new Seat(i, SeatCategory.SILVER));
        }

        for(int i = 40; i<80; i++){
            seats.add(new Seat(i, SeatCategory.GOLD));
        }
        for(int i = 80; i<120; i++){
            seats.add(new Seat(i, SeatCategory.PLATINUM));
        }

        return  seats;
    }

    private List<Screen> createScreen() {

        List<Screen> screens = new ArrayList<>();
        for(int i = 0; i<10; i++) {
            Screen screen1 = new Screen(i);
            screen1.setSeats(createSeats());
            screens.add(screen1);
        }
        return screens;
    }

    private Show createShows(int showId, Screen screen, Movie movie, int showTime){
       return  new Show(showId,screen,movie,showTime);
    }

    private  void createTheatres(){
    Movie avengersMovie = movieController.getMovieByName("Avengers");
    Movie bahubaliMovie = movieController.getMovieByName("Baahubali");

    Theatre inoxTheatre = new Theatre(1, "Telangana", City.Hyderabad);
    inoxTheatre.setScreenList(createScreen());

    inoxTheatre.addShow(createShows(1,inoxTheatre.getScreenList().get(0),avengersMovie,9 ));

    inoxTheatre.addShow(createShows(2, inoxTheatre.getScreenList().get(1),bahubaliMovie,8 ));

    Theatre pvrTheatre = new Theatre(2, "TamilNadu", City.Chennai);
    pvrTheatre.setScreenList(createScreen());

        pvrTheatre.addShow(createShows(1,inoxTheatre.getScreenList().get(0),avengersMovie,9 ));

        pvrTheatre.addShow(createShows(2, inoxTheatre.getScreenList().get(1),bahubaliMovie,8 ));

 theatreController.addTheatre(inoxTheatre, City.Hyderabad);
 theatreController.addTheatre(pvrTheatre, City.Chennai);


    }
}
