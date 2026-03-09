import Entity.Pool;
import Entity.User;
import Entity.Booking;
import Exception.PoolNotFoundException;
import Exception.UserNotFoundException;
import Repository.PoolRepository;
import Repository.UserRepository;
import Service.BookingService;
import Service.PoolService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Hello, World!");

            UserRepository userRepository = new UserRepository();
            PoolRepository poolRepository = new PoolRepository();

            User user = new User("12", "Bhavana");
            User user2 = new User("23", "Minnie");
            userRepository.save(user);
            userRepository.save(user2);

            BookingService bookingService = new BookingService(userRepository, poolRepository);
            PoolService poolService = new PoolService(userRepository, poolRepository);
            poolService.createPool(user.userId(), "Hyd", "Blr", Instant.ofEpochSecond(12), 2);
            List<Pool> poolList = poolService.searchBySourceDes("Hyd", "Blr");

            System.out.println(poolList);

            if (poolList.isEmpty()) {
                System.out.println("No pools found for given route");
                return;
            }

            Pool selectedPool = poolList.get(0);
            System.out.println("Selected poolId: " + selectedPool.getPoolId());

            Optional<Booking> booking1 = bookingService.createBooking(user2.userId(), selectedPool.getPoolId());
            Optional<Booking> booking2 = bookingService.createBooking(user2.userId(), selectedPool.getPoolId());
            Optional<Booking> booking3 = bookingService.createBooking(user2.userId(), selectedPool.getPoolId());

            System.out.println("Booking 1 success: " + booking1.isPresent());
            System.out.println("Booking 2 success: " + booking2.isPresent());
            System.out.println("Booking 3 success: " + booking3.isPresent());

        } catch (PoolNotFoundException | UserNotFoundException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }
}