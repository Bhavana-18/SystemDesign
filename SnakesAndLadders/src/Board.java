import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 100;
    private final List<Snake> snakes;
    private final List<Ladder> ladders;


    Board( ){
        snakes = new ArrayList<>();
        ladders = new ArrayList<>();
        initializeSnakesAndLadders();
    }

    void initializeSnakesAndLadders(){
        // Initialize snakes
        snakes.add(new Snake(1,16, 6));
        snakes.add(new Snake(2,48, 26));
        snakes.add(new Snake(3,64, 60));
        snakes.add(new Snake(4,93, 73));

        // Initialize ladders
        ladders.add(new Ladder(1,1, 38));
        ladders.add(new Ladder(2, 4, 14));
        ladders.add(new Ladder(3, 9, 31));
        ladders.add(new Ladder(4, 21, 42));
        ladders.add(new Ladder(5, 28, 84));
        ladders.add(new Ladder(6, 51, 67));
        ladders.add(new Ladder(7,80, 99));

    }

    public int getNextPosition(int currentPosition){
        for(Snake snake : snakes){
            if(snake.getStart() == currentPosition)
                return snake.getEnd();
        }
        for(Ladder ladder : ladders){
            if(ladder.getStart() == currentPosition)
                return ladder.getEnd();
        }

        return currentPosition;
    }

    public static int getBoardSize() {
        return BOARD_SIZE;
    }
}
