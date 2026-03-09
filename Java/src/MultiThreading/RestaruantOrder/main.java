package MultiThreading.RestaruantOrder;

public class main {
    public static void main(String[] args){
        String order = "order A";
        Waiter waiter = new Waiter(order);
        Chef chef = new Chef(order);
        waiter.start();
        chef.start();
    }
}
