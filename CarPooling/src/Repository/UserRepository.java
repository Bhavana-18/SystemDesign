package Repository;

import Entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void save(User user){
        if(user != null)
        users.put(user.userId(), user);
        else
        throw new IllegalArgumentException("user cannot be null ");
    }

    public boolean existsById(String userId){
        return users.containsKey(userId);
    }

    public Optional<User> findById(String userId){
        if(users.containsKey(userId)){
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

}
