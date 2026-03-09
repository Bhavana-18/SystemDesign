package MultiThreading.RestaruantOrder;

public class Waiter extends Thread {
    private Object order;
    Waiter(Object order){
        this.order = order;
    }

    @Override
    public void  run(){
        synchronized (order){
            try{
                System.out.println("order is passed to chef, waiting for order to get ready");
                order.wait();
                System.out.println("Order notified to waiter, Passing to the customer");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
