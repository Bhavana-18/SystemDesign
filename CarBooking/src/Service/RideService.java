package Service;

import Entity.Driver;
import Entity.Location;
import Entity.Ride;
import Entity.Rider;
import Enums.DriverStatus;
import Enums.RideStatus;
import Repository.DriverRepository;
import Repository.RideRepository;
import Repository.RiderRepository;
import dto.RideEstimate;
import util.EtaEstimator;
import util.PricingCalculator;

import java.util.UUID;

public class RideService {
    private final DriverService driverService;
    private final RiderRepository riderRepository;
    private final RideRepository rideRepository;
    private final RiderService riderService;
    private final MatchingStrategy matchingStrategy;
    private final DriverRepository driverRepository;

    public RideService(DriverService driverService, RiderRepository riderRepository, RideRepository rideRepository, RiderService riderService, MatchingStrategy matchingStrategy, DriverRepository driverRepository){
        this.driverService = driverService;
        this.rideRepository = rideRepository;
        this.riderRepository = riderRepository;
        this.riderService = riderService;
        this.matchingStrategy = matchingStrategy;
        this.driverRepository = driverRepository;
    }
    public RideEstimate searchRide(Location pickup, Location drop){
        if(pickup == null || drop == null)
            throw new IllegalArgumentException("Pickup or drop cannot be null");
        Driver driver = matchingStrategy.findBestDriver(pickup, driverRepository.getDriverList());

        if(driver == null)
            throw new RuntimeException("No driver available");
        double estimatedFare = PricingCalculator.calculateFare(pickup, drop);
        double estimatedEta = EtaEstimator.estimateEtaInMinutes(driver.getLocation(), pickup);
        return new RideEstimate(estimatedFare, estimatedEta);
    }

    public Ride requestRide(String riderId, Location pickup, Location drop){
        Rider rider = riderService.getRiderOrThrow(riderId);
        if(pickup == null || drop == null){
            throw new IllegalArgumentException("Pickup or drop cannot be  null");
        }
        Driver driver = matchingStrategy.findBestDriver(pickup,  driverRepository.getDriverList());
        rider.getLock().lock();
        Ride ride = null;
        try{
            driver.getLock().lock();
            try{
                if(driver.getDriverStatus() != DriverStatus.AVAILABLE)
                    throw new IllegalArgumentException("Driver is already occupied");
                //or else match again till timeout
                String rideId = UUID.randomUUID().toString();
                double fare = PricingCalculator.calculateFare(pickup, drop);
                ride = new Ride(rideId, riderId, driver.getDriverId(), pickup, drop, fare);

                driver.setDriverStatus(DriverStatus.OCCUPIED);
                rider.setActiveRideId(rideId);
                driverRepository.save(driver);
                riderRepository.save(rider);
                rideRepository.save(ride);
            }
            finally{
                driver.getLock().unlock();
            }
        } finally{
            rider.getLock().unlock();
        }

     return ride;

    }

    public void startRide(String rideId){
        Ride ride = getRideOrThrow(rideId);
        ride.getLock().lock();
        try{
            if(ride.getRideStatus() != RideStatus.ASSIGNED)
                throw new IllegalArgumentException("Ride cannot be started from current state");
            ride.setRideStatus(RideStatus.STARTED);
            rideRepository.save(ride);
        } finally {
            ride.getLock().unlock();
        }

    }

    public void endRide(String rideId){
        Ride ride = getRideOrThrow(rideId);
        Driver driver = driverService.getDriverOrThrow(ride.getDriverId());
        Rider rider = riderService.getRiderOrThrow(ride.getRiderId());
        rider.getLock().lock();
        try{
            driver.getLock().lock();
        try{
        ride.getLock().lock();
        try{
            if(ride.getRideStatus() != RideStatus.STARTED)
                throw new IllegalArgumentException("Ride cannot be ended from current state");
            ride.setRideStatus(RideStatus.ENDED);
            driver.setDriverStatus(DriverStatus.AVAILABLE);
            rider.setActiveRideId(null);

            driverRepository.save(driver);
            riderRepository.save(rider);
            rideRepository.save(ride);

        } finally {
            ride.getLock().unlock();
        } }finally{
            driver.getLock().unlock();
            }} finally{
            rider.getLock().unlock();
            }

    }

    public Ride getRideOrThrow(String rideId){
        if(rideId == null || rideId.isBlank())
            throw new IllegalArgumentException("Ride is not found");
        return rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
    }
}
