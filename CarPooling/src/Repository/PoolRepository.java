package Repository;

import Entity.Pool;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PoolRepository {
    private final Map<String, Pool> poolMap = new ConcurrentHashMap<>();

    public void save(Pool pool){
        if(pool != null)
            poolMap.put(pool.getPoolId(), pool);
    }

    public boolean existsById(String poolId){
        return poolMap.containsKey(poolId);
    }

    public Optional<Pool> findById(String poolId){
        return Optional.ofNullable(poolMap.get(poolId));
    }

    public List<Pool> searchBySourceAndDestination(String source, String  des){
        List<Pool> poolList = new ArrayList<>();
        for(var e : poolMap.entrySet()){
            Pool pool = e.getValue();
            if(pool.getStartDestination().equals(source) && pool.getEndDestination().equals(des))
                poolList.add(pool);
        }
        return poolList;
    }
}
