package Entity;

import java.time.Instant;

public class InventoryRecord {
    private final String skuId;
    private int availableQty;
    private int reservedQty;

    private final Instant createdAt;
    private Instant updatedAt;

    public InventoryRecord(String skuId, int initialAvailableQty) {
        if (skuId == null || skuId.isBlank()) {
            throw new IllegalArgumentException("skuId cannot be null/blank");
        }
        if (initialAvailableQty < 0) {
            throw new IllegalArgumentException("Initial quantity cannot be negative");
        }

        this.skuId = skuId;
        this.availableQty = initialAvailableQty;
        this.reservedQty = 0;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getSkuId() {
        return skuId;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public int getReservedQty() {
        return reservedQty;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void addStock(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.availableQty += qty;
        this.updatedAt = Instant.now();
    }

    public void reserve(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.availableQty < qty) {
            throw new IllegalStateException("Insufficient inventory");
        }
        this.availableQty -= qty;
        this.reservedQty += qty;
        this.updatedAt = Instant.now();
    }

    public void confirmReserved(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.reservedQty < qty) {
            throw new IllegalStateException("Insufficient reserved inventory");
        }
        this.reservedQty -= qty;
        this.updatedAt = Instant.now();
    }

    public void releaseReserved(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.reservedQty < qty) {
            throw new IllegalStateException("Insufficient reserved inventory");
        }
        this.reservedQty -= qty;
        this.availableQty += qty;
        this.updatedAt = Instant.now();
    }
}