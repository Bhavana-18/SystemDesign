package OrderProcessingStrategy;

public class Customer {
    private String name;
    private String address;
    Customer(String name, String  address){
        this.address = address;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
