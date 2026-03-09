package Entity;
import Service.BalanceManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

        private final String id;
        private final String name;
        private final Set<String> members = ConcurrentHashMap.newKeySet();
        private final BalanceManager ledger = new BalanceManager();
        private final List<Expense> expenses = Collections.synchronizedList(new ArrayList<>());


    public Group(String id, String name, Collection<String> memberIds) {
            this.id = Objects.requireNonNull(id);
            this.name = Objects.requireNonNull(name);
            if (memberIds != null) members.addAll(memberIds);
        }

        public String id() { return id; }
        public String name() { return name; }
        public Set<String> members() { return Collections.unmodifiableSet(members); }
        public BalanceManager ledger() { return ledger; }

        public void addMember(String userId) { members.add(userId); }
        public boolean hasMember(String userId) { return members.contains(userId); }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        synchronized (expenses){
            return new ArrayList<>(expenses);
        }
    }

    public BalanceManager getLedger() {
        return ledger;
    }
}
