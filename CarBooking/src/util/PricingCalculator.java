package util;


import Entity.Location;

public  class PricingCalculator {

    private static final double BASE_FARE = 50.0;
    private static final double PER_KM_RATE = 10.0;

    public static double calculateFare(Location pickup, Location drop) {
        double distance = pickup.distanceTo(drop);

        return BASE_FARE + (distance * PER_KM_RATE);
    }
}