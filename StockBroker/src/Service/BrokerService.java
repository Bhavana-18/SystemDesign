package Service;

import Entity.Order;
import Entity.OrderBook;

import Entity.UserAccount;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class BrokerService {
    // Storage
    private final Map<String, UserAccount> users = new ConcurrentHashMap<>();
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final Map<String, OrderBook> books = new ConcurrentHashMap<>();

    //Locks
    private final Map<String, ReentrantLock> userLocks = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> symbolLocks = new ConcurrentHashMap<>();

    private ReentrantLock userLock(String userId){
        return  userLocks.computeIfAbsent(userId, k-> new ReentrantLock());
    }

    private ReentrantLock symbolLock(String symbol){
        return symbolLocks.computeIfAbsent(symbol, k-> new ReentrantLock());
    }

    //User Operations

    public void  createUser(String userId, String name){
        users.putIfAbsent(userId, new UserAccount(userId, name));
    }

    public void deposit(String userId, long amount){
        ReentrantLock lock = userLock(userId);
        lock.lock();
        try{
            users.get(userId).deposit(amount);
        } finally {
            lock.unlock();
        }
    }
    public void withdraw(String userId, long amount){
        ReentrantLock lock = userLock(userId);
        lock.lock();
        try{
            users.get(userId).withdraw(amount);
        } finally {
            lock.unlock();
        }
    }

    public void addSymbol(String symbol) {
        books.putIfAbsent(symbol, new OrderBook(symbol));
    }

    //Order Placement

    public String placeBuy(String userId, String symbol, long qty, long price){
        OrderBook book = books.get(symbol);
        ReentrantLock sLock = symbolLock(symbol);
        sLock.lock();
        try{
            ReentrantLock uLock = userLock(userId);
            try{
                uLock.lock();
                UserAccount ua = users.get(userId);
                ua.reserveCash(price *qty);
               Order order = Order.limitBuy(userId, symbol, qty, price);
               orders.put(order.getOrderId(), order);
               book.add(order);
               match(symbol);
               return order.getOrderId();


            } finally {
                uLock.unlock();
            }
        } finally {
            sLock.unlock();
        }
    }

    public String placeSell(String userId, String symbol, long qty, long price) {
        OrderBook book = books.get(symbol);
        ReentrantLock sLock = symbolLock(symbol);
        sLock.lock();
        try {
            ReentrantLock uLock = userLock(userId);
            uLock.lock();
            try {
                UserAccount ua = users.get(userId);
                ua.reserveHolding(symbol, qty);

                Order order = Order.limitSell(userId, symbol, qty, price);
                orders.put(order.getOrderId(), order);
                book.add(order);

                match(symbol);
                return order.getOrderId();
            } finally {
                uLock.unlock();
            }
        } finally {
            sLock.unlock();
        }
    }
    public void cancel(String orderId) {
        Order order = orders.get(orderId);
        String symbol = order.getSymbol();
        ReentrantLock sLock = symbolLock(symbol);
        sLock.lock();
        try {
            ReentrantLock uLock = userLock(order.getUserId());
            uLock.lock();
            try {
                if (order.isTerminal()) return;
                long remaining = order.remainingQty();
                order.cancel();

                UserAccount ua = users.get(order.getUserId());
                if (order.isBuy())
                    ua.releaseCash(remaining * order.getLimitPrice());
                else
                    ua.releaseHolding(symbol, remaining);

            } finally {
                uLock.unlock();
            }
        } finally {
            sLock.unlock();
        }
    }
 private void match(String symbol){
        OrderBook book = books.get(symbol);
        while(true){
            Order buy = peekActive(book.getBuyPQ());
            Order sell = peekActive(book.getSellList());
            if(buy == null || sell == null)return;
            if(buy.getLimitPrice() < sell.getLimitPrice()) return;
            long qty = Math.min(buy.remainingQty(), sell.remainingQty());
            long execPrice = sell.getLimitPrice();
            String u1 = buy.getUserId();
            String u2 = sell.getUserId();
            String first = u1.compareTo(u2) <= 0? u1: u2;
            String second = first.equals(u1) ? u2: u1;
            ReentrantLock l1 = userLock(first);
            ReentrantLock l2 = userLock(second);
            l1.lock();
            l2.lock();
            try{
                UserAccount buyer = users.get(buy.getUserId());
                UserAccount seller = users.get(sell.getUserId());

                long reservedUsed = qty* buy.getLimitPrice();
                long actualCost = qty * execPrice;

                buyer.spendReservedCash(reservedUsed);
                buyer.refundToAvailable(reservedUsed - actualCost);
                buyer.addHolding(symbol,qty);
                seller.consumeReservedHolding(symbol, qty);
                seller.creditCash(actualCost);
                buy.fill(qty);
                sell.fill(qty);
            } finally{
                l2.unlock();
                l1.unlock();
            }
        }

 }

 private  Order peekActive(PriorityQueue<Order> pq){
        while(!pq.isEmpty()){
            Order o = pq.peek();
            if(o.isActive()) return o;
            pq.poll();
        }
        return null;
 }


    public Order getOrder(String buyOrderId) {
        return orders.get(buyOrderId);
    }
    public void seedHolding(String userId, String symbol, long qty) {
        users.get(userId).addHolding(symbol, qty);
    }
}
