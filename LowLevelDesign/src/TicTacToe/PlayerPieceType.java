package TicTacToe;

public enum PlayerPieceType {
    playerX('X'),
    playerO('O');
    private final char value;

    PlayerPieceType(char value) {
        this.value = value;
    }

    public char getValue(){
        return this.value;
    }
}
