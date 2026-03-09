package Service;

import Entity.Booking;
import Entity.Pool;
import Entity.User;
import Repository.PoolRepository;
import Repository.UserRepository;
import Exception.PoolNotFoundException;
import Exception.UserNotFoundException;

import java.util.Optional;


public class BookingService {

    private final UserRepository userRepository;
    private final PoolRepository poolRepository;

    public BookingService(UserRepository userRepository, PoolRepository poolRepository){
        this.poolRepository = poolRepository;
        this.userRepository = userRepository;
    }

    public Optional<Booking> createBooking(String userId, String poolId) {
        User user = getUserOrThrow(userId);

        Pool pool = getPoolOrThrow(poolId);
        return pool.book(userId);
    }


    private User getUserOrThrow(String userId){
        if(userId == null || userId.isBlank()){
            throw new IllegalArgumentException("userId cannot be null or blank");
        }
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found" + userId));

    }

    private Pool getPoolOrThrow(String poolId){
        if (poolId == null || poolId.isBlank()) {
            throw new IllegalArgumentException("poolId cannot be null or blank");
        }
        return  poolRepository.findById(poolId).orElseThrow(() -> new PoolNotFoundException("Pool not found:" + poolId));
    }
}
