package OrderProcessingStrategy;

import java.util.ArrayList;
import java.util.List;

public class Main {

        public static void main(String[] args) {
            Customer customer1 = new Customer("Bhavana", "India");
            Product product1 = new Product("gold" , 30.5);
            List<Product> products = new ArrayList<>();
            products.add(product1);
            Order order = new Order(customer1, products, new ExpressOrderProcessor());
            order.processOrder();

            System.out.println("Hello, World!");
        }

}
