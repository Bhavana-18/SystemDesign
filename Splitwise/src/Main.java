import Entity.Settlement;
import Entity.SplitType;
import Service.BalanceManager;
import Service.ExpenseService;
import Service.GroupService;
import Service.SimplifyDebtService;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class Main {
    public static void main(String[] args) {
        GroupService groupService = new GroupService();
        ExpenseService expenseService = new ExpenseService(groupService);
        SimplifyDebtService simplifyDebtService = new SimplifyDebtService();

        // Add users
        expenseService.addUser("U1", "Alice", "alice@test.com");
        expenseService.addUser("U2", "Bob", "bob@test.com");
        expenseService.addUser("U3", "Charlie", "charlie@test.com");
        expenseService.addUser("U4", "David", "david@test.com");

        // Create group
        groupService.createGroup("G1", "Goa Trip", List.of("U1", "U2", "U3", "U4"));

        // 1) Equal split
        // Alice paid 1200 for Alice, Bob, Charlie
        expenseService.addExpense(
                "G1",
                "U1",
                1200,
                SplitType.EQUAL,
                List.of("U1", "U2", "U3"),
                null
        );

        // 2) Exact split
        // Bob paid 1000 for Bob, Charlie, David with exact amounts
        expenseService.addExpense(
                "G1",
                "U2",
                1000,
                SplitType.EXACT,
                List.of("U2", "U3", "U4"),
                List.of(200L, 300L, 500L)
        );

        // 3) Percent split
        // Charlie paid 2000 for Alice, Charlie, David
        // 50%, 25%, 25%
        expenseService.addExpense(
                "G1",
                "U3",
                2000,
                SplitType.PERCENT,
                List.of("U1", "U3", "U4"),
                List.of(50L, 25L, 25L)
        );

        // Print raw group balances
        System.out.println("==== Raw Group Balances ====");
        printBalances(expenseService.getGroupBalanceSnapshot("G1"));

        // Simplified settlements
        System.out.println("\n==== Simplified Settlements ====");
        List<Settlement> settlements =
                simplifyDebtService.simplify(expenseService.getGroupBalanceSnapshot("G1"));

        for (Settlement s : settlements) {
            System.out.println(
                    s.fromUserId() + " pays " + s.toUserId() + " " + format(s.amountPrice())
            );
        }

        // Optional: perform one settlement
        if (!settlements.isEmpty()) {
            Settlement first = settlements.getFirst();
            expenseService.settle("G1", first.fromUserId(), first.toUserId(), first.amountPrice());

            System.out.println("\n==== Balances After One Settlement ====");
            printBalances(expenseService.getGroupBalanceSnapshot("G1"));
        }
    }

    private static void printBalances(java.util.Map<String, java.util.Map<String, Long>> snapshot) {
        if (snapshot.isEmpty()) {
            System.out.println("No balances");
            return;
        }

        for (var e : snapshot.entrySet()) {
            String debtor = e.getKey();
            for (var inner : e.getValue().entrySet()) {
                String creditor = inner.getKey();
                long amount = inner.getValue();
                if (amount > 0) {
                    System.out.println(debtor + " owes " + creditor + " " + format(amount));
                }
            }
        }
    }

    private static String format(long rupees) {
        return String.valueOf(rupees);
    }
}