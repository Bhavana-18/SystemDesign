package Entity;

public class Holding {
    private final  String symbol;
    private long availableQty;
    private long reservedQty;

    public Holding(String symbol){
        if(symbol == null || symbol.isBlank()){
            throw new IllegalArgumentException("symbol cannot be null/blank");
        }

        this.symbol = symbol;
        this.availableQty = 0L;
        this.reservedQty = 0L;
    }

    public long getAvailableQty() {
        return availableQty;
    }

    public long getReservedQty() {
        return reservedQty;
    }

    public String getSymbol() {
        return symbol;
    }

    public void reserve(long qty){
        if(qty <= 0){
            throw new IllegalArgumentException("Reserve qty must be >0");
        }
        if(availableQty < qty){
            throw new IllegalArgumentException("Insufficient available quantity");
        }
        availableQty -= qty;
        reservedQty += qty;
    }

    public void release(long qty){
        if (qty <= 0)
            throw new IllegalArgumentException("Release qty must be > 0");

        if (reservedQty < qty)
            throw new IllegalStateException("Insufficient reserved quantity");

        reservedQty -= qty;
        availableQty += qty;
    }
    // Called when trade executes (SELL side)
    public void consumeReserved(long qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("Consume qty must be > 0");

        if (reservedQty < qty)
            throw new IllegalStateException("Insufficient reserved quantity");

        reservedQty -= qty;
    }

    // Called when BUY trade executes
    public void add(long qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("Add qty must be > 0");

        availableQty += qty;
    }
}
