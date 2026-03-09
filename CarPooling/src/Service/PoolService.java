package Service;

import Entity.Pool;
import Repository.PoolRepository;
import Repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PoolService {

    private final UserRepository userRepository;
    private final PoolRepository poolRepository;

    public PoolService(UserRepository userRepository , PoolRepository poolRepository){
        this.poolRepository = poolRepository;
        this.userRepository = userRepository;
    }

    public Pool createPool(String userId, String source, String destination,
                           Instant startTime, int capacity) {
        validateUser(userId);

        String poolId = UUID.randomUUID().toString();
        Pool pool = new Pool(poolId, userId, startTime, capacity, source, destination);
        poolRepository.save(pool);
        return pool;
    }

    private void validateUser(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId cannot be null or blank");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
    }

    public List<Pool> searchBySourceDes(String source, String destination) {
        return poolRepository.searchBySourceAndDestination(source, destination);
    }
}
