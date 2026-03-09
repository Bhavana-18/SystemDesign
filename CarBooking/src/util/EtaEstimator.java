package util;

import Entity.Location;

public class EtaEstimator {
    private static final double AVERAGE_SPEED_KMH = 40.0;

    public  static double estimateEtaInMinutes(Location driverLocation, Location pickup) {
        double distance = driverLocation.distanceTo(pickup);

        double hours = distance / AVERAGE_SPEED_KMH;

        return hours * 60;
    }
}
