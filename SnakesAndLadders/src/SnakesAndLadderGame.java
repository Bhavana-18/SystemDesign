import java.util.ArrayList;
import java.util.List;

public class SnakesAndLadderGame {
    List<Player> playerList;

    Board board;
    private final Dice dice;
    private int currentPlayerIndex;
   // private  boolean hasAWinner = false;

    SnakesAndLadderGame  (List<String> playerNames){
        dice = new Dice();
        playerList = new ArrayList<>();
        board = new Board();
        for(String player : playerNames){
            playerList.add(new Player(player,0));
        }

    }
    public void play(){
        while(!isGameOver()){
            Player currentPlayer = playerList.get(currentPlayerIndex);
            int diceRoll = dice.roll();
            int newPosition = currentPlayer.getCurrentPosition() + diceRoll;

            if(newPosition <= Board.getBoardSize()){
                currentPlayer.setCurrentPosition(board.getNextPosition(newPosition));
                System.out.println("Player " + currentPlayer.getName() + " moved to " + currentPlayer.getCurrentPosition());
            }

            if(currentPlayer.getCurrentPosition() == Board.getBoardSize()){
                System.out.println("Player " + currentPlayer.getName() + " `wins!");
                break;
            }
            currentPlayerIndex = (currentPlayerIndex + 1) %playerList.size();
        }
    }

    public boolean isGameOver(){
        for(Player player:playerList){
            if(player.getCurrentPosition() == Board.getBoardSize())
                return  true;
        }
        return false;
    }


}
