public class PhoneDisplay implements  Observer{
    String weather;

    @Override
    public void update(String weather){
        this.weather = weather;
        display();
    }

    public void display(){
        System.out.println("Phone Display weather :" + weather);
    }
}
