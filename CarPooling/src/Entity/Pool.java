package Entity;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Pool {
    private final String poolId;
    private final String userId;
    private final Map<String, Booking> bookingsById;
    private final String startDestination;
    private final String endDestination;
    private final Instant startTime;
    private final int capacity;

    public Pool(String poolId, String userId, Instant startTime, int capacity, String startDestination, String endDestination){
        this.poolId = poolId;
        this.userId = userId;
        this.bookingsById = new ConcurrentHashMap<>();
        this.startTime = startTime;
        this.capacity = capacity;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
    }

    public String getUserId() {
        return userId;
    }

    public int getCapacity() {
        return capacity;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public String getPoolId() {
        return poolId;
    }

    public Map<String, Booking> getBookingsById() {
        return Collections.unmodifiableMap(bookingsById);
    }

    public synchronized Optional<Booking> book(String userId){
        if(userId == null || userId.isBlank()){
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if(isFull()){
            throw new IllegalArgumentException("Capacity is full");
        }
        Booking booking = new Booking(userId, UUID.randomUUID().toString());
        bookingsById.put(booking.bookingId(), booking);

        return Optional.of(booking);
    }

    public boolean isFull(){
        return bookingsById.size() >= capacity;
    }

    public String getEndDestination() {
        return endDestination;
    }

    public String getStartDestination() {
        return startDestination;
    }
}
