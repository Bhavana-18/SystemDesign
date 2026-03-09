package Service;

import Entity.Split;

import java.util.*;

public class ExactSplitStrategy implements SplitStrategy {
    @Override
    public List<Split> compute(long amountPaise, List<String> participants, List<Long> values) {
        if (participants == null || values == null || participants.size() != values.size()) {
            throw new IllegalArgumentException("participants and exact values mismatch");
        }

        long sum = 0;
        List<Split> splits = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            long share = values.get(i);
            if (share < 0) throw new IllegalArgumentException("negative share");
            sum += share;
            splits.add(new Split(participants.get(i), share));
        }

        if (sum != amountPaise) {
            throw new IllegalArgumentException("exact shares do not sum to amount");
        }
        return splits;
    }
}
