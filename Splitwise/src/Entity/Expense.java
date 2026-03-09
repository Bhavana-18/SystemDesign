package Entity;

import java.util.List;

public record Expense(String id, String payerId, long amount, SplitType splitType, List<Split> splitList) {
}
