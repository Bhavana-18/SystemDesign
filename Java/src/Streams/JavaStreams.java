package Streams;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class JavaStreams {
    public static Optional<Integer> findSecondHighestInteger(List<Integer> numbers){
        return  numbers.stream().distinct().sorted(Collections.reverseOrder()).skip(1).findFirst();
    }

    public static int removeOddMultiplyConstant(List<Integer> numbers, int constant){
        return  numbers.stream().filter(n-> n%2 == 0).map(n-> n*constant).mapToInt(Integer:: intValue).sum();
    }
    public static int findLongestLength(List<String> num){
        return  num.stream().map(e-> e.length()).max((a, b) -> Integer.compare(a, b)).get();
    }
    public static long getTotalNoOfWords(List<String > sentences){
        return sentences.stream().flatMap(e -> Arrays.stream(e.split(" "))).distinct().count();

    }
    public static String joinFirstTwoWords(List<String> strings){
        return strings.stream().filter(e -> e.length()%2 == 0).limit(2).collect(Collectors.joining());
    }
    public static int sumOfArray(List<Integer> num){
        return  num.stream().mapToInt(e -> e* e).sum();
    }
    public static double avgList(List<Integer> arr){
        return arr.stream().mapToInt(e -> e).average().getAsDouble();
    }
    public static List<String > convertToLowerCase(List<String> strings){
        return  strings.stream().map(String::toLowerCase).toList();
    }
    public static List<String>

    public static void main(String[] args){
        List<Integer> nums = Arrays.asList(1,2,3,3,4,5,5,6,9,9);
        Optional<Integer> result = findSecondHighestInteger(nums);
        nums.stream().map(n-> n*n).toList();

        System.out.println(result);

    }

}
