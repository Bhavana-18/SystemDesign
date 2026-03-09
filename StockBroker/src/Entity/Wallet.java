package Entity;

public class Wallet {


    private long availableCash;
    private long reservedCash;

    public Wallet(){
      this.availableCash = 0L;
      this.reservedCash = 0L;
    }

    public long getAvailableCash() {
        return availableCash;
    }

    public long getReservedCash() {
        return reservedCash;
    }

    public void deposit(long depositAmt){
        if(depositAmt <= 0){
            throw new IllegalArgumentException("Deposit amount should be greater than 0");
        }
        this.availableCash += depositAmt;
    }

    public void withdraw(long amount){
        if(amount > availableCash){
            throw new IllegalArgumentException("withdraw amount should not be greater than available cash");
        }
        this.availableCash -= amount;

    }

    public void reserveCash(long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Reserve amount should be > 0");
        }
        if(availableCash < amount){
            throw new IllegalArgumentException("Insufficient available balance to reserve");
        }

        this.availableCash -= amount;
        this.reservedCash += amount;
    }

    public void releaseCash(long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Spend amount must be > 0");
        }
        if(amount > reservedCash){
            throw new IllegalArgumentException("Insufficient reserved balance");
        }
        this.reservedCash -= amount;
        this.availableCash -= amount;
    }

    public void spendReservedCash(long amount){
        if(amount <= 0)
            throw new IllegalArgumentException("Spend amount must be > 0");
        if(reservedCash < amount)
            throw new IllegalArgumentException("Insufficient reserved balance");
        reservedCash -= amount;
    }

    public void refundToAvailable(long amount){
        if(amount < 0)
            throw new IllegalArgumentException("Refund amount cannot be negative");
        availableCash += amount;
    }
}
