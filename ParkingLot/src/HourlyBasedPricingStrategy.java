public class HourlyBasedPricingStrategy implements  PricingStrategy{

    @Override
    public double calculatePrice(long duration, VehicleType vehicle) {
        duration =  (duration / (1000 * 60 * 60)) + 1;
        switch(vehicle){
            case VehicleType.FourWheeler ->
            {
                return 10* duration;
            }

            case VehicleType.TwoWheeler ->
            {
                return 5 * duration;
            }
        }
        return 10;
    }

}
