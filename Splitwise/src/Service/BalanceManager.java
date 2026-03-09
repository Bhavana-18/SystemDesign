package Service;

import java.util.HashMap;
import java.util.Map;

public class BalanceManager {
    private final Map<String , Map<String, Long>> bal = new HashMap<>();

    public synchronized void addDebt(String debtor, String creditor, long amount ){
        if(debtor == null || creditor == null) return;
        if(debtor.equals(creditor))  return;
        if(amount <= 0) return;

        Map<String , Long> revMap = bal.get(creditor);
        long rev = 0L;
        if(revMap != null) rev = revMap.getOrDefault(debtor, 0L);
        if(rev > 0){
            long settle = Math.min(rev, amount);
            long newRev = rev - settle;
            if(newRev == 0){
                revMap.remove(debtor);
                if(revMap.isEmpty()) bal.remove(revMap);
            } else{
                revMap.put(debtor, newRev);
            }
            amount -= settle;
            if(amount == 0) return;
        }

        Map<String,Long> debtorMap = bal.computeIfAbsent(debtor, k-> new HashMap<>());
        long curr = debtorMap.getOrDefault(creditor, 0L);
        debtorMap.put(creditor, curr + amount);

    }

    public synchronized  Map<String, Long> getBalancesByUser(String userId){
        Map<String , Long> m = bal.get(userId);
        if(m == null) return  new HashMap<>();
        return  new HashMap<>(m);
    }

    public synchronized  Map<String, Map<String, Long>> getAllBalancesSnapshot(){
        Map<String, Map<String, Long>> out = new HashMap<>();
        for (Map.Entry<String, Map<String, Long>> e : bal.entrySet()) {
            out.put(e.getKey(), new HashMap<>(e.getValue()));
        }
        return out;
    }
}
