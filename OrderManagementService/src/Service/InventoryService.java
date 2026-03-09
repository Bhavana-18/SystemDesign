package Service;

import Entity.OrderItem;

import java.util.List;

public interface InventoryService {
    void addStock(String  skuId, int qty);
    void reserveItems(List<OrderItem> orderItemList);
    void releaseItems(List<OrderItem> orderItemList);

}
