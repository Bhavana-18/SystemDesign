package TicTacToe;

public class Board {
    private final char[][] board ;
    private  int countMoves;

    Board(){
   this.board = new char[3][3];
        initializeGame();
    }
     void initializeGame(){
        countMoves = 0;
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                this.board[i][j] = '-';
            }
        }

    }
    public boolean isFull(){
        if(countMoves == 9){
            System.out.println("Board is full");
            return true;
        }

        return false;
    }

    public boolean hasWinner(){
        for(int i = 0; i< 3; i++){
            if(board[i][0] != '-' && board[i][0] == board[i][1]&& board[i][1] ==board[i][2])
                return true;
            if(board[0][i] != '-' && board[0][i] == board[1][i]  && board[1][i] == board[2][i])
                return true;
        }

        if(board[0][0] != '-' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return true;
        if(board[2][0] != '-' && board[0][2] ==  board[1][1] &&  board[1][1] == board[2][0] )
            return true;
        return false;
    }

    public boolean addPiece(PlayerPieceType playerPieceType, int row, int col){

        if(row >= 0 && row <3 && col>=0 && col <3 && board[row][col] == '-' ){
            board[row][col] = playerPieceType.getValue();
            countMoves++;
            return true;
        } else{
            throw new IllegalArgumentException("Invalid Move");
        }
    }

    public void printBoard(){
        for(int i = 0 ; i<3; i++){
            for(int j = 0; j< 3 ; j++){
                System.out.print(board[i][j] + " | ");
            }
            System.out.println(" ");
        }
    }
}
