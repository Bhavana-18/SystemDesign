import java.util.*;

public class Demo {
    public int suitableLocations(int[] centers, int d) {
        Arrays.sort(centers);
        int n = centers.length;

        // Define the search space (a bit wider than min/max center)
        int min = centers[0] - d / 2;
        int max = centers[n - 1] + d / 2;

        int result = 0;

        // Prefix sum to help with cost calculation
        int[] prefixSum = new int[n];
        prefixSum[0] = centers[0];
        for (int i = 1; i < n; i++) {
            prefixSum[i] = prefixSum[i - 1] + centers[i];
        }

        // Start scanning x from min to max
        int leftCount = 0;  // # centers <= x
        int rightCount = n; // # centers > x

        int idx = 0;
        long cost = 0;
        for (int c : centers)
            cost += 2L * Math.abs(min - c);

        for (int x = min; x <= max; x++) {
            if (x != min) {
                // Update left/right count as x increases
                while (idx < n && centers[idx] < x) {
                    leftCount++;
                    rightCount--;
                    idx++;
                }
                // Move from x-1 to x: delta cost = 2 * (leftCount - rightCount)
                cost += 2L * (leftCount - rightCount);
            }

            if (cost <= d) result++;
        }

        return result;
    }

    // You can test with this main method
    public static void main(String[] args) {
       Demo sol = new Demo();
        int[] centers = {-2, 1, 0};
        int d = 8;
        System.out.println(sol.suitableLocations(centers, d)); // Output: 3
    }
}