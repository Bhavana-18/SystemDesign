package Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserAccount {
    private final String userId;
    private final User user;
    private final Wallet wallet;
    private final Map<String, Holding> holdingsBySymbol;

    public UserAccount(String userId, String userName){
        if (userId == null || userId.isBlank())
            throw new IllegalArgumentException("userId cannot be null/blank");
        if (userName == null || userName.isBlank())
            throw new IllegalArgumentException("userName cannot be null/blank");
        this.userId = userId;
        this.user = new User(userId, userName);
        this.wallet = new Wallet();
        this.holdingsBySymbol = new HashMap<>();

    }

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public Map<String, Holding> getHoldings() {
        return holdingsBySymbol;
    }

    public Wallet getWallet() {
        return wallet;
    }
    public void ensureActive() {
        user.ensureActive();
    }

    public void activate() {
        user.activate();
    }

    public void deactivate() {
        user.deactivate();
    }
    public void deposit(long amount) {
        ensureActive();
        wallet.deposit(amount);
    }

    public void withdraw(long amount) {
        ensureActive();
        wallet.withdraw(amount); // only from available
    }

    public long availableCash() {
        return wallet.getAvailableCash();
    }

    public long reservedCash() {
        return wallet.getReservedCash();
    }

    public void reserveCash(long amount) {
        ensureActive();
        wallet.reserveCash(amount);
    }

    public void releaseCash(long amount) {
        ensureActive();
        wallet.releaseCash(amount);
    }

    public void spendReservedCash(long amount) {
        ensureActive();
        wallet.spendReservedCash(amount);
    }

    public void refundToAvailable(long amount) {
        ensureActive();
        wallet.refundToAvailable(amount);
    }
    private Holding holdingOrCreate(String symbol) {
        Objects.requireNonNull(symbol, "symbol cannot be null");
        if (symbol.isBlank()) throw new IllegalArgumentException("symbol cannot be blank");
        return holdingsBySymbol.computeIfAbsent(symbol, Holding::new);
    }

    public long availableQty(String symbol) {
        Holding h = holdingsBySymbol.get(symbol);
        return (h == null) ? 0L : h.getAvailableQty();
    }

    public long reservedQty(String symbol) {
        Holding h = holdingsBySymbol.get(symbol);
        return (h == null) ? 0L : h.getReservedQty();
    }

    public Map<String, Holding> getHoldingsView() {
        // For interview: return as-is or create defensive copy later
        return holdingsBySymbol;
    }

    public void reserveHolding(String symbol, long qty) {
        ensureActive();
        holdingOrCreate(symbol).reserve(qty);
    }

    public void releaseHolding(String symbol, long qty) {
        ensureActive();
        holdingOrCreate(symbol).release(qty);
    }

    // SELL settlement: shares already reserved at order placement
    public void consumeReservedHolding(String symbol, long qty) {
        ensureActive();
        holdingOrCreate(symbol).consumeReserved(qty);
    }

    // BUY settlement: buyer receives shares
    public void addHolding(String symbol, long qty) {
        ensureActive();
        holdingOrCreate(symbol).add(qty);
    }

    // SELL settlement: seller receives money
    public void creditCash(long amount) {
        ensureActive();
        wallet.deposit(amount);
    }
}
