package Entity;

import java.math.BigDecimal;

public class OrderItem {
    private final String skuId;
    private int quantity;
    private BigDecimal unitPriceAtPurchase;

    public OrderItem(String skuId, int quantity, BigDecimal unitPriceAtPurchase){
        this.quantity = quantity;
        this.skuId = skuId;
        this.unitPriceAtPurchase = unitPriceAtPurchase;
    }

    public String getSkuId() {
        return skuId;
    }

    public BigDecimal getUnitPriceAtPurchase() {
        return unitPriceAtPurchase;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPriceAtPurchase(BigDecimal unitPriceAtPurchase) {
        this.unitPriceAtPurchase = unitPriceAtPurchase;
    }
}
