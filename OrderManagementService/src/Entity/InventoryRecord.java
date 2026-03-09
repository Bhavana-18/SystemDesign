package Entity;

import java.time.Instant;
import java.util.Objects;

public class InventoryRecord {

    private final String skuId;
    private int availableQty;

    private final Instant createdAt;
    private Instant updatedAt;

    public InventoryRecord(String skuId, int initialQty) {
        if (initialQty < 0) {
            throw new IllegalArgumentException("Initial quantity cannot be negative");
        }
        this.skuId = Objects.requireNonNull(skuId);
        this.availableQty = initialQty;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getSkuId() {
        return skuId;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Call only under skuLock
    public void increase(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.availableQty += qty;
        this.updatedAt = Instant.now();
    }

    // Call only under skuLock
    public void decrease(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.availableQty < qty) {
            throw new IllegalStateException("Insufficient inventory");
        }
        this.availableQty -= qty;
        this.updatedAt = Instant.now();
    }
}