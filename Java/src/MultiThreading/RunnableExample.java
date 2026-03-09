package MultiThreading;

public class RunnableExample implements Runnable{
    String task;
    RunnableExample(String task){
        this.task = task;
    }
    @Override
    public void run(){
        System.out.println("This is run by Runnable class" + task + Thread.currentThread().getName());
        
    }
}

class Maain {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnableExample("xyz"));
        thread1.start();

    }
}
