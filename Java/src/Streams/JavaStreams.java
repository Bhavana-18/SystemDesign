package Streams;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaStreams {
    public static Optional<Integer> findSecondHighestInteger(List<Integer> numbers){
        return  numbers.stream().distinct().sorted(Collections.reverseOrder()).skip(1).findFirst();
    }
    public static void main(String[] args){
        List<Integer> nums = Arrays.asList(1,2,3,3,4,5,5,6,9,9);
        Optional<Integer> result = findSecondHighestInteger(nums);
        System.out.println(result);

    }

}
