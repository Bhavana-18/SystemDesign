import Entity.Order;
import Service.BrokerService;

public class Main {

    public static void main(String[] args) {

        BrokerService broker = new BrokerService();

        // ---- Setup symbol ----
        broker.addSymbol("TCS");

        // ---- Create users ----
        broker.createUser("U1", "Alice");
        broker.createUser("U2", "Bob");

        // ---- Fund buyer ----
        broker.deposit("U1", 1_000_000); // ₹10,000 (paise)

        // ---- Seed seller with shares ----
        broker.seedHolding("U2", "TCS", 10);

        // ---- Place orders ----
        String buyOrderId = broker.placeBuy("U1", "TCS", 10, 100_00);  // Buy 10 @ 100
        String sellOrderId = broker.placeSell("U2", "TCS", 10, 95_00); // Sell 10 @ 95

        // ---- Fetch results ----
        Order buy = broker.getOrder(buyOrderId);
        Order sell = broker.getOrder(sellOrderId);

        System.out.println("Buy Order Status  : " + buy.getStatus());
        System.out.println("Sell Order Status : " + sell.getStatus());
        System.out.println("Buy Filled Qty    : " + buy.getFilledQty());
        System.out.println("Sell Filled Qty   : " + sell.getFilledQty());

        System.out.println("Execution successful.");
    }
}