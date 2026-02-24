import java.security.spec.ECField;
import java.util.concurrent.Semaphore;

public class EvenOddPrinter {

    private int number = 1;
    private final int limit ;

    private final Semaphore odd = new Semaphore(1);
    private final Semaphore even  = new Semaphore(0);

    public EvenOddPrinter(int limit){
        this.limit = limit;
    }

    public void printOdd() throws InterruptedException{
        while(number <= limit){
            odd.acquire();
            if(number <= limit){
                System.out.println(number + " ");
                number++;
            }
            even.release();
        }
    }
    public void printEven() throws InterruptedException{
        while(number <= limit){
            even.acquire();
            if(number <= limit){
                System.out.println(number + " ");
                number++;
            }
            odd.release();
        }
    }
}
