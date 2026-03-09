package Entity;

import java.util.*;

public class OrderBook {
    private String symbol;

    PriorityQueue<Order> buyList = new PriorityQueue<>(Comparator.comparingLong(Order::getLimitPrice).reversed()
            .thenComparingLong(Order:: getCreatedAt));
    PriorityQueue<Order> sellList = new PriorityQueue<>(Comparator.comparingLong(Order::getLimitPrice).thenComparingLong(Order::getCreatedAt));

    public OrderBook(String symbol) {
      if(symbol == null || symbol.isBlank()) throw new IllegalArgumentException("symbol cannot be null or empty");
      this.symbol = symbol;
    }
    public String getSymbol(){
        return symbol;
    }
    public PriorityQueue<Order> getBuyPQ(){
        return buyList;
    }

    public PriorityQueue<Order> getSellList() {
        return sellList;
    }

    public void add(Order order){
        if (!symbol.equals(order.getSymbol())) {
            throw new IllegalArgumentException("Order symbol mismatch");
        }
        if (order.isBuy()) buyList.add(order);
        else sellList.add(order);
    }

}
