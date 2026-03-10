package Service;

import Entity.OrderItem;

import java.util.List;

public interface InventoryService {
    void addStock(String skuId, int qty);
    void reserveItems(List<OrderItem> orderItems);
    void confirmReservedItems(List<OrderItem> orderItems);
    void releaseItems(List<OrderItem> orderItems);
    int getAvailableStock(String skuId);
}