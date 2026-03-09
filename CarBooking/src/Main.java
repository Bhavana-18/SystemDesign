

import Entity.Driver;
import Entity.Location;
import Entity.Ride;
import Entity.Rider;
import Repository.DriverRepository;
import Repository.RideRepository;
import Repository.RiderRepository;
import Service.DriverService;
import Service.RideService;
import Service.RiderService;
import dto.RideEstimate;
import Service.MatchingStrategy;
import Service.NearestDriverMatchingStrategy;

public class Main {
    public static void main(String[] args) {
        try {
            // Repositories
            RiderRepository riderRepository = new RiderRepository();
            DriverRepository driverRepository = new DriverRepository();
            RideRepository rideRepository = new RideRepository();

            // Services
            RiderService riderService = new RiderService(riderRepository);
            DriverService driverService = new DriverService(driverRepository);

            // Strategy
            MatchingStrategy matchingStrategy = new NearestDriverMatchingStrategy();

            // Ride service
            RideService rideService = new RideService(
                    driverService,
                    riderRepository,
                    rideRepository,
                    riderService,
                    matchingStrategy,
                    driverRepository
            );

            // Register rider
            Rider rider = riderService.registerRider("r1", "Bhavana");
            System.out.println("Registered Rider: " + rider);

            // Register drivers
            Driver d1 = driverService.registerDriver("Driver-1", "d1@test.com", "LIC-101");
            Driver d2 = driverService.registerDriver("Driver-2", "d2@test.com", "LIC-102");
            Driver d3 = driverService.registerDriver("Driver-3", "d3@test.com", "LIC-103");

            System.out.println("Registered Drivers:");
            System.out.println(d1);
            System.out.println(d2);
            System.out.println(d3);

            // Update driver locations
            driverService.updateDriverLocation(d1.getDriverId(), new Location(10, 10));
            driverService.updateDriverLocation(d2.getDriverId(), new Location(4, 4));
            driverService.updateDriverLocation(d3.getDriverId(), new Location(20, 20));

            // Mark drivers available
            driverService.updateDriverStatus(d1.getDriverId(), Enums.DriverStatus.AVAILABLE);
            driverService.updateDriverStatus(d2.getDriverId(), Enums.DriverStatus.AVAILABLE);
            driverService.updateDriverStatus(d3.getDriverId(), Enums.DriverStatus.AVAILABLE);

            Location pickup = new Location(5, 5);
            Location drop = new Location(15, 15);

            // Search ride estimate
            RideEstimate estimate = rideService.searchRide(pickup, drop);
            System.out.println("Ride Estimate: " + estimate);

            // Request ride
            Ride ride = rideService.requestRide(rider.getRiderId(), pickup, drop);
            System.out.println("Ride Booked: " + ride);

            // Start ride
            rideService.startRide(ride.getRideId());
            System.out.println("Ride Started: " + rideService.getRideOrThrow(ride.getRideId()));

            // End ride
            rideService.endRide(ride.getRideId());
            System.out.println("Ride Ended: " + rideService.getRideOrThrow(ride.getRideId()));

            // Final states
            System.out.println("Final Rider State: " + riderService.getRiderOrThrow(rider.getRiderId()));
            System.out.println("Final Driver State: " + driverService.getDriverOrThrow(ride.getDriverId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}