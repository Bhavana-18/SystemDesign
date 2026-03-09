package Entity;


import java.util.UUID;

public class Trade {
    private final String tradeId = UUID.randomUUID().toString();
    private final String symbol;
    private final long qty;
    private final long price; // paise
    private final String buyOrderId;
    private final String sellOrderId;
    private final long timestamp = System.currentTimeMillis();

    public Trade(String symbol, long qty, long price, String buyOrderId, String sellOrderId) {
        this.symbol = symbol;
        this.qty = qty;
        this.price = price;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
    }

    public String getTradeId() { return tradeId; }
    public String getSymbol() { return symbol; }
    public long getQty() { return qty; }
    public long getPrice() { return price; }
    public String getBuyOrderId() { return buyOrderId; }
    public String getSellOrderId() { return sellOrderId; }
    public long getTimestamp() { return timestamp; }
}