import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private String highestAverage(String[][] scores){
        Map<String, Double> scoresMap = Arrays.stream(scores)
                .collect(Collectors.groupingBy(
                s ->s[0],
                Collectors.averagingInt(s -> Integer.parseInt(s[1]))));

        return scoresMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}