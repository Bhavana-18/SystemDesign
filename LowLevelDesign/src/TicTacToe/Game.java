package TicTacToe;

import java.util.Scanner;

public class Game {
    Player player1;
    Player player2;
    Player currentPlayer;
    Board board;

    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        board = new Board();
        startGame();

    }
    void startGame(){
        System.out.println("Game starts now");
        while(!board.isFull() && !board.hasWinner()){
            System.out.println(currentPlayer.getName() + "'s turn ");
            int row = getValidInput("Enter row number between 0 ad 2 -");
            int col = getValidInput("Enter column number between 0 and 2 -");
            board.addPiece(currentPlayer.getPlayerPieceType(), row, col);
            board.printBoard();
            switchPlayer();

            if(board.hasWinner()){
                switchPlayer();
                System.out.println("Game over!!" + currentPlayer.getName()+ " wins!");
            }
        }
    }

    void switchPlayer(){
       currentPlayer= currentPlayer == player1 ? player2: player1;
    }

    private int getValidInput(String message){
        Scanner  scanner = new Scanner(System.in);
        while(true){
            System.out.println(message);
            if(scanner.hasNextInt()){
                int input = scanner.nextInt();
                if(input>= 0 && input <3){
                    return input;
                }

            } else{

                scanner.next();
            }
            System.out.println("Invalid input. Please  enter a number between 0 and 2");
        }

    }
}
