package Service;


import Entity.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseService {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final AtomicLong expenseSeq = new AtomicLong(1);
    private final GroupService groupService;
    private final Map<SplitType, SplitStrategy> strategyMap = Map.of(
            SplitType.EQUAL, new EqualSplitStrategy(),
            SplitType.EXACT, new ExactSplitStrategy(),
            SplitType.PERCENT, new PercentSplitStrategy()
    );


    public ExpenseService(GroupService groupService){
        this.groupService = groupService;
    }

    public void addUser(String id, String name, String email){
        if(id == null || id.isBlank()) throw  new IllegalArgumentException("Id must not be null ot blank");
        users.put(id, new User(id, name , email));
    }

    public String addExpense(String groupId, String payerId, long amount, SplitType splitType, List<String> participants, List<Long> values){
        validateUser(payerId);
        Group group = groupService.group(groupId);
        if(amount <= 0) throw new IllegalArgumentException("amount must be > 0");
        if(!group.hasMember(payerId)){
            throw new IllegalArgumentException("payer not in group" + payerId);
        }
        if(participants == null || participants.isEmpty()) throw new IllegalArgumentException("Participants size should be min 2");
        Set<String> uniq = new LinkedHashSet<>(participants);
        List<String> orderedParticipants = new ArrayList<>(uniq);
        if(uniq.size() != participants.size()) throw new IllegalArgumentException("FOund duplicate participants");
        for(String u : uniq) {
            validateUser(u);
            if(!group.hasMember(u)){
                throw new IllegalArgumentException("participant not in group" + u);
            }
        }
        int n = uniq.size();
        SplitStrategy strategy = strategyMap.get(splitType);
        if(strategy == null){
            throw new IllegalArgumentException("unsupported split type");
        }
        List<Split> splitList = strategy.compute(amount, orderedParticipants,values);

         BalanceManager balanceManager = group.getLedger();
         for(Split split : splitList){
             if(!split.userId().equals(payerId) && split.amount()>0 ){
                 balanceManager.addDebt(split.userId(), payerId, split.amount());
             }
         }
        String id = "E" + expenseSeq.getAndIncrement();
        group.getExpenses().add(new Expense(id, payerId, amount, splitType, Collections.unmodifiableList(splitList)));
        return id;

    }

    public void settle(String groupId, String fromUserId, String toUserId, long amountPaise) {
        validateUser(fromUserId);
        validateUser(toUserId);
        if (amountPaise <= 0) throw new IllegalArgumentException("amount must be > 0");
        Group group = groupService.group(groupId);
        if(!group.hasMember(fromUserId) || !group.hasMember(toUserId)){
            throw new IllegalArgumentException("users must belong to group");
        }
        group.getLedger().addDebt(toUserId, fromUserId, amountPaise);
    }


//    public void showBalancesForUser(String userId) {
//        validateUser(userId);
//
//        Map<String, Map<String, Long>> snapshot = balanceManager.getAllBalancesSnapshot();
//        boolean found = false;
//
//        for (Map.Entry<String, Map<String, Long>> e : snapshot.entrySet()) {
//            String debtor = e.getKey();
//            for (Map.Entry<String, Long> inner : e.getValue().entrySet()) {
//                String creditor = inner.getKey();
//                long amount = inner.getValue();
//
//                if (amount == 0) continue;
//
//                if (debtor.equals(userId)) {
//                    System.out.println(userId + " owes " + creditor + " " + format(amount));
//                    found = true;
//                } else if (creditor.equals(userId)) {
//                    System.out.println(debtor + " owes " + userId + " " + format(amount));
//                    found = true;
//                }
//            }
//        }
//
//        if (!found) {
//            System.out.println("No balances");
//        }
//    }

    public Map<String, Map<String, Long>> getGroupBalanceSnapshot(String groupId) {
        return groupService.group(groupId).getLedger().getAllBalancesSnapshot();
    }


    private String format(long paise) {
        return String.format("%.2f", paise / 100.0);
    }

    private void validateUser(String userId){
        if(!users.containsKey(userId)) throw  new IllegalArgumentException("unkown user" + userId);
    }

}
