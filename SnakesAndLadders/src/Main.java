import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        List<String> players = new ArrayList<>();
        players.add("Manvitha");
        players.add("Bhavana");
        players.add("Shreya");
        players.add("Priya");

        SnakesAndLadderGame game = new SnakesAndLadderGame(players);
        System.out.println("Game starts!!");
        game.play();
        System.out.println("Game ends!!");

    }
}