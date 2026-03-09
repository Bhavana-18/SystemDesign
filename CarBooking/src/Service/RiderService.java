package Service;

import Exception.RiderNotFoundException;
import Entity.Rider;
import Repository.RiderRepository;

public class RiderService {
    private final RiderRepository riderRepository;

    public RiderService(RiderRepository repository){
        this.riderRepository = repository;
    }

    public Rider registerRider(String userId, String userName){
        if(userId == null || userName == null || userId.isBlank() || userName.isBlank())
            throw new IllegalArgumentException("UserId or name cannot be null or blank");

        Rider rider = new Rider(userId, userName);
        riderRepository.save(rider);
        return rider;
    }

    public Rider getRiderOrThrow(String riderId){
        if(riderId == null || riderId.isBlank()){
            throw new IllegalArgumentException("RiderId cannot be blank or null");
        }
        return riderRepository.findById(riderId).orElseThrow(()-> new RiderNotFoundException("Rider not found"));
    }

//    public void updateActiveRide(String riderId, String rideId) {
//        if (riderId == null || riderId.isBlank() || rideId == null || rideId.isBlank()) {
//            throw new IllegalArgumentException("Rider id or ride id cannot be null or blank");
//        }
//
//        Rider rider = getRiderOrThrow(riderId);
//        rider.getLock().lock();
//        try {
//            rider.setActiveRideId(rideId);
//            riderRepository.save(rider);
//        } finally {
//            rider.getLock().unlock();
//        }
//    }
//
//    public void clearActiveRide(String riderId) {
//        if (riderId == null || riderId.isBlank()) {
//            throw new IllegalArgumentException("Rider id cannot be null or blank");
//        }
//
//        Rider rider = getRiderOrThrow(riderId);
//        rider.getLock().lock();
//        try {
//            rider.setActiveRideId(null);
//            riderRepository.save(rider);
//        } finally {
//            rider.getLock().unlock();
//        }
//    }

}
