package Service;

import Entity.Split;

import java.util.*;

public class PercentSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> compute(long amountPaise, List<String> participants, List<Long> values) {
        if (participants == null || values == null || participants.size() != values.size()) {
            throw new IllegalArgumentException("participants and percent values mismatch");
        }

        long percentSum = 0;
        for (Long p : values) {
            if (p == null || p < 0) throw new IllegalArgumentException("invalid percent");
            percentSum += p;
        }

        if (percentSum != 100) {
            throw new IllegalArgumentException("percent must sum to 100");
        }

        List<Split> splits = new ArrayList<>();
        long assigned = 0;

        for (int i = 0; i < participants.size(); i++) {
            long share = (amountPaise * values.get(i)) / 100;
            assigned += share;
            splits.add(new Split(participants.get(i), share));
        }

        long remainder = amountPaise - assigned;

        for (int i = 0; i < remainder; i++) {
            Split old = splits.get(i);
            splits.set(i, new Split(old.userId(), old.amount() + 1));
        }

        return splits;
    }
}
