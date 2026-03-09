package MultiThreading;

public class ThreadExample extends  Thread {
    String task;
    ThreadExample(String task){
        this.task = task;
    }

    public void run(){
        System.out.println("Running the task: " + task + Thread.currentThread().getName());

    }

}

class Main {
    public static void main(String[] args) {
        Thread thread1 = new ThreadExample("xyz");
        thread1.start();

    }
}
