package Entity;

import Enums.ProductStatus;

import java.math.BigDecimal;

public class StockKeepingUnit {
    private final String skuId;
    private  String name;

    private  BigDecimal price;
    private ProductStatus productStatus;

    public StockKeepingUnit(String skuId, String name, BigDecimal price, ProductStatus productStatus){
        this.skuId = skuId;
        this.name = name;
        this.price = price;
        this.productStatus = productStatus;
    }

    public String getName() {
        return name;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSkuId() {
        return skuId;
    }
}
