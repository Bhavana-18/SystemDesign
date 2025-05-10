public class Seat {
    private final int seatNo;
    private  int row;
    private final SeatCategory seatCategory;
    private SeatStatus seatStatus;

    Seat(int seatNo,  SeatCategory seatCategory){
        this.seatCategory = seatCategory;
        this.seatNo = seatNo;
//        this.row = row;
        this.seatStatus = SeatStatus.AVAILABLE;

    }

    public int getSeatNo() {
        return seatNo;
    }

    public SeatCategory getSeatCategory() {
        return seatCategory;
    }

    public int getRow() {
        return row;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public  void  setSeatStatus(SeatStatus seatStatus){
        this.seatStatus = seatStatus;
    }
}
