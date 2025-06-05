package OrderProcessingStrategy;

public class ExpressOrderProcessor implements  ProcessingStrategy{
    @Override
    public void process(Order order){
        double sum = 0.0;
        for(Product product: order.getProducts()){
            sum += product.getPrice();
        }
        sum += 5.0;
        System.out.println("Total price is " + sum);
    }
}
