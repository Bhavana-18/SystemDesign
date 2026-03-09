package Service;

import Entity.Split;

import java.util.*;

public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> compute(long amountPaise, List<String> participants, List<Long> values) {
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("participants required");
        }

        int n = participants.size();
        long base = amountPaise / n;
        long remainder = amountPaise % n;

        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            long share = base + (i < remainder ? 1 : 0);
            splits.add(new Split(participants.get(i), share));
        }
        return splits;
    }
}
