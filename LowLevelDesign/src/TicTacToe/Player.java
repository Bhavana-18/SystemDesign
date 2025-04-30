package TicTacToe;

public class Player {

    private final String name;
    private final PlayerPieceType playerPieceType;

    public Player(String name, PlayerPieceType playerPieceType){
        this.name = name;
        this.playerPieceType = playerPieceType;
    }
    public String getName(){
        return this.name;
    }
    public PlayerPieceType getPlayerPieceType(){
        return this.playerPieceType;
    }


}
