import TicTacToe.Game;
import TicTacToe.Player;
import TicTacToe.PlayerPieceType;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        Player player1 = new Player("Bhavana", PlayerPieceType.playerX);
        Player player2 = new Player("Sahithi", PlayerPieceType.playerO);

        Game game = new Game(player1, player2);



    }
}