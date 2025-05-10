public class TvDisplay implements  Observer{
    String weather;

    @Override
    public void update(String weather){
        this.weather = weather;
        display();
    }

    public void display(){
        System.out.println("TvDisplay weather :" + weather);
    }
}
