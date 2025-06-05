package OrderProcessingStrategy;

import java.util.List;

public class Order {
    private List<Product> products;
    private Customer customer;
    private ProcessingStrategy processingStrategy;
    Order(Customer customer , List<Product> products , ProcessingStrategy processingStrategy){
        this.customer = customer;
        this.products = products;
        this.processingStrategy = processingStrategy;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ProcessingStrategy getProcessingStrategy() {
        return processingStrategy;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setProcessingStrategy(ProcessingStrategy processingStrategy) {
        this.processingStrategy = processingStrategy;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public  void processOrder(){
        processingStrategy.process(this);
    }
 }
