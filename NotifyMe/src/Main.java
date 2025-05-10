public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        WeatherStation weatherStation= new WeatherStation();
        weatherStation.addObserver(new PhoneDisplay());
        weatherStation.addObserver(new TvDisplay());
        weatherStation.setWeather("cool");
        weatherStation.notifyObservers();
    }
}