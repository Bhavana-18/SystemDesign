package Service;

import Entity.Split;

import java.util.List;

public interface SplitStrategy {
    List<Split> compute(long amountPaise, List<String> participants, List<Long> values);
}
