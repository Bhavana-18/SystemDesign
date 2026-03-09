package Service;

import Entity.Settlement;
import Entity.User;

import java.util.*;

public class SimplifyDebtService {
    public List<Settlement>  simplify(Map<String, Map<String, Long>> snapshot){
        Map<String, Long> net = new HashMap<>();
        for(var e: snapshot.entrySet()){
            String debtor = e.getKey();
            for(var inner : e.getValue().entrySet()){
                String creditor = inner.getKey();
                long amt = inner.getValue();
                if(amt <= 0) continue;
                net.put(debtor, net.getOrDefault(debtor, 0L) - amt);
                net.put(creditor, net.getOrDefault(creditor, 0L) + amt);
            }
        }

        PriorityQueue<UserAmt> debtors = new PriorityQueue<>((a, b) -> Long.compare(b.amt, a.amt));
        PriorityQueue<UserAmt> creditors = new PriorityQueue<>((a, b) -> Long.compare(b.amt, a.amt));

        for(var e: net.entrySet()){
            String user = e.getKey();
            long amt = e.getValue();
            if(amt < 0) debtors.add(new UserAmt(user, -amt));
            else if (amt > 0) creditors.add(new UserAmt(user, amt));
        }
        List<Settlement> res = new ArrayList<>();
        while(!creditors.isEmpty() && !debtors.isEmpty()){
            UserAmt c = creditors.poll();
            UserAmt d = debtors.poll();
            long pay = Math.min(c.amt, d.amt);
            res.add(new Settlement(d.userId, c.userId, pay));
            long cRem = c.amt - pay;
            long dRem = d.amt - pay;
            if(cRem > 0) creditors.add(new UserAmt(c.userId, cRem));
            if(dRem > 0) debtors.add(new UserAmt(d.userId, dRem));
        }
        return  res;

    }

    private  static class UserAmt{
        final String userId;
        final long amt;
        UserAmt(String userId, long amt){
            this.userId = userId;
            this.amt = amt;
        }
    }
}
