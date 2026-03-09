package MultiThreading.RestaruantOrder;

public class Chef  extends  Thread{
    private Object order;
    Chef(Object object){
        this.order = object;
    }

    @Override
    public void run (){
        try {
            Thread.sleep(2000);
            synchronized (order){
                    System.out.println("Order is ready");
                    order.notify();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
