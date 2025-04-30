import java.util.ArrayList;
import java.util.List;

public class Elevator {
private int id;
private int capacity;
private int currentFloor;
private Direction currentDirection;
private List<Request> requestList;

Elevator(int id, int capacity){
    this.id = id;
    this.capacity = capacity;
    this.currentFloor = 0;
    this.currentDirection = Direction.UP;
    requestList = new ArrayList<>(capacity);
}

public synchronized  void addRequest(Request request){
    if(requestList.size() < capacity){
        requestList.add(request);
        System.out.println("Request added to elevator" + request.getStartFloor() + "to" + request.getDestinationFloor()+ ":" + id );
       notifyAll();
    }
}
public synchronized Request getNextRequest(){
    while(requestList.isEmpty()){
        try{
            wait();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    return  requestList.removeFirst();

}
public synchronized  void processRequests(){
    while(true){
        while(!requestList.isEmpty()){
            Request request = getNextRequest();
            processRequest(request);
        }
        try{
            wait();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
    public void processRequest(Request request){
      int startFloor = currentFloor;
      int destinationFloor = request.getDestinationFloor();
      if(startFloor < destinationFloor){
          currentDirection = Direction.UP;
          for(int i = startFloor ; i<= destinationFloor; i++){
              System.out.println("Elevator id" + id + "reached floor" + i);
              try{
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }

          }
      }  else if(startFloor > destinationFloor) {
          currentDirection = Direction.DOWN;
          for(int i = startFloor ; i>= destinationFloor; i--){

              System.out.println("Elevator id" + id + "reached floor" + i);
              try{
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }

          }
      }

    }
    public void run() {
        processRequests();
    }


    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }
}
