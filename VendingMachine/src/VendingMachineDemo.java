// Core model class
import java.util.*;
class Product {
    private final String id;
    private final String name;
    private final double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public double getPrice() { return price; }
    public String getName() { return name; }
    public String getId() { return id; }
}

// Inventory Manager - SRP: manages product quantities
class InventoryManager {
    private final Map<Product, Integer> stock = new HashMap<>();

    public void addProduct(Product product) {
        stock.put(product, stock.getOrDefault(product, 0) + 1);
    }

    public boolean isAvailable(Product product) {
        return stock.getOrDefault(product, 0) > 0;
    }

    public void decrement(Product product) {
        stock.computeIfPresent(product, (k, v) -> (v > 1) ? v - 1 : null);
    }
}

// State Interface
interface VendingMachineState {
    void handle(VendingMachineContext context);
}

// Vending Machine Context
class VendingMachineContext {
    private VendingMachineState currentState;
    private final InventoryManager inventoryManager = new InventoryManager();
    private Product selectedProduct;
    private double insertedAmount = 0.0;

    public void setState(VendingMachineState state) {
        this.currentState = state;
    }

    public void start() {
        currentState.handle(this);
    }

    public void selectProduct(Product product) {
        this.selectedProduct = product;
    }

    public void insertAmount(double amount) {
        this.insertedAmount += amount;
    }

    public Product getSelectedProduct() { return selectedProduct; }
    public double getInsertedAmount() { return insertedAmount; }
    public InventoryManager getInventoryManager() { return inventoryManager; }
    public void reset() {
        selectedProduct = null;
        insertedAmount = 0.0;
    }
}

// Idle State
class IdleState implements VendingMachineState {
    private final Product product;

    public IdleState(Product product) {
        this.product = product;
    }

    public void handle(VendingMachineContext context) {
        if (!context.getInventoryManager().isAvailable(product)) {
            System.out.println("Product out of stock: " + product.getName());
            return;
        }
        context.selectProduct(product);
        context.setState(new PaymentState());
    }
}

// Payment State
class PaymentState implements VendingMachineState {
    public void handle(VendingMachineContext context) {
        Product product = context.getSelectedProduct();
        System.out.println("Insert coins for: " + product.getName() + " - Price: " + product.getPrice());
        // simulate coin insert
        context.insertAmount(product.getPrice());
        context.setState(new DispenseState());
    }
}

// Dispense State
class DispenseState implements VendingMachineState {
    public void handle(VendingMachineContext context) {
        Product product = context.getSelectedProduct();
        if (context.getInsertedAmount() < product.getPrice()) {
            System.out.println("Insufficient amount. Please insert more coins.");
            return;
        }
        context.getInventoryManager().decrement(product);
        System.out.println("Dispensing: " + product.getName());
        double change = context.getInsertedAmount() - product.getPrice();
        if (change > 0) System.out.println("Returning change: " + change);
        context.reset();
        context.setState(new DoneState());
    }
}

// Done / Reset State
class DoneState implements VendingMachineState {
    public void handle(VendingMachineContext context) {
        System.out.println("Transaction complete. Ready for next user.");
    }
}

// Main Demo
public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachineContext machine = new VendingMachineContext();
        Product chips = new Product("1", "Chips", 10.0);

        machine.getInventoryManager().addProduct(chips);

        // Start interaction
        machine.setState(new IdleState(chips));
        machine.start(); // Select product
        machine.start(); // Insert coin
        machine.start(); // Dispense product
        machine.start(); // Done
    }
}
