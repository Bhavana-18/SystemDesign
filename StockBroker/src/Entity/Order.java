package Entity;

import Enums.OrderStatus;
import Enums.Side;

import java.util.Objects;
import java.util.UUID;

public class Order {

    private final String orderId;
    private final String userId;
    private final String symbol;

    private final Side side;              // BUY / SELL
    private final long qty;               // total qty
    private long filledQty;               // executed so far
    private final long limitPrice;        // paise/cents
    private final long createdAt;         // for FIFO tie-break

    private OrderStatus status;           // OPEN/PARTIALLY_FILLED/FILLED/CANCELLED

    private Order(String orderId, String userId, String symbol, Side side, long qty, long limitPrice, long createdAt) {
        this.orderId = Objects.requireNonNull(orderId, "orderId");
        this.userId = validate(userId, "userId");
        this.symbol = validate(symbol, "symbol");
        this.side = Objects.requireNonNull(side, "side");

        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (limitPrice <= 0) throw new IllegalArgumentException("limitPrice must be > 0");

        this.qty = qty;
        this.limitPrice = limitPrice;
        this.createdAt = createdAt;

        this.filledQty = 0L;
        this.status = OrderStatus.OPEN;
    }

    public static Order limitBuy(String userId, String symbol, long qty, long limitPrice) {
        return new Order(UUID.randomUUID().toString(), userId, symbol, Side.BUY, qty, limitPrice, System.currentTimeMillis());
    }

    public static Order limitSell(String userId, String symbol, long qty, long limitPrice) {
        return new Order(UUID.randomUUID().toString(), userId, symbol, Side.SELL, qty, limitPrice, System.currentTimeMillis());
    }

    private static String validate(String v, String name) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(name + " cannot be null/blank");
        return v;
    }

    // ---- getters ----
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public String getSymbol() { return symbol; }
    public Side getSide() { return side; }
    public long getQty() { return qty; }
    public long getFilledQty() { return filledQty; }
    public long getLimitPrice() { return limitPrice; }
    public long getCreatedAt() { return createdAt; }
    public OrderStatus getStatus() { return status; }

    public boolean isBuy() { return side == Side.BUY; }
    public boolean isSell() { return side == Side.SELL; }

    public long remainingQty() {
        return qty - filledQty;
    }

    public boolean isActive() {
        return status == OrderStatus.OPEN || status == OrderStatus.PARTIALLY_FILLED;
    }

    public boolean isTerminal() {
        return status == OrderStatus.FILLED || status == OrderStatus.CANCELLED;
    }

    // ---- state changes (called under symbol lock; user locks when settling) ----

    public void fill(long execQty) {
        if (execQty <= 0) throw new IllegalArgumentException("execQty must be > 0");
        if (!isActive()) throw new IllegalStateException("Cannot fill order in state: " + status);
        if (execQty > remainingQty()) throw new IllegalStateException("execQty exceeds remainingQty");

        filledQty += execQty;

        if (filledQty == qty) status = OrderStatus.FILLED;
        else status = OrderStatus.PARTIALLY_FILLED;
    }

    public void cancel() {
        if (status == OrderStatus.CANCELLED) return; // idempotent
        if (status == OrderStatus.FILLED) throw new IllegalStateException("Cannot cancel FILLED order");
        status = OrderStatus.CANCELLED;
    }
}