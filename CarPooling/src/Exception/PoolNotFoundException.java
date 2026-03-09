package Exception;

public class PoolNotFoundException extends RuntimeException{
    public PoolNotFoundException(String message){
        super(message);
    }
}
