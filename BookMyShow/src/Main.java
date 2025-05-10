public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        BookMyShow bookMyShow = new BookMyShow();

//        bookMyShow.initialize();

        //user1
        bookMyShow.createBooking(City.Hyderabad, "Baahubali");
        //user2
        bookMyShow.createBooking(City.Hyderabad, "Baahubali");

    }
}