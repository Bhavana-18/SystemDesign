package OrderProcessingStrategy;

public class StandardOrderProcessor implements  ProcessingStrategy {

    @Override
    public void process(Order order){
        double sum = 0.0;
        for(Product product: order.getProducts()){
            sum += product.getPrice();
        }
        System.out.println("Total price is " + sum);
    }
}
