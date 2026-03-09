import java.io.*;
import java.util.*;

/**
 * Count teams of 3 (i<j<k) such that:
 *   Ri < Rj < Rk  OR  Ri > Rj > Rk
 *
 * Optimized: O(N log N) using Fenwick Tree (BIT) + coordinate compression.
 * Uses long to avoid overflow: max teams = nC3 fits in long for typical constraints.
 */
public class Main {

    // Fenwick Tree for frequencies (1-indexed)
    static final class Fenwick {
        private final int n;
        private final long[] bit;

        Fenwick(int n) {
            this.n = n;
            this.bit = new long[n + 1];
        }

        void add(int idx, long delta) {
            for (int i = idx; i <= n; i += i & -i) bit[i] += delta;
        }

        long sum(int idx) { // prefix sum [1..idx]
            long res = 0;
            for (int i = idx; i > 0; i -= i & -i) res += bit[i];
            return res;
        }
    }

    public static long countTeams(int[] a) {
        int n = a.length;
        if (n < 3) return 0L;

        // Coordinate compression (ratings are distinct, but we still compress for BIT size)
        int[] sorted = a.clone();
        Arrays.sort(sorted);
        // Since all distinct, rank is (lower_bound + 1)
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            int r = Arrays.binarySearch(sorted, a[i]); // 0..n-1
            rank[i] = r + 1; // 1..n
        }

        Fenwick left = new Fenwick(n);
        Fenwick right = new Fenwick(n);

        // init right with all elements
        for (int r : rank) right.add(r, 1);

        long ans = 0L;
        long seenLeft = 0L;         // number of elements processed on left side
        long remainingRight = n;    // number of elements currently in right BIT (before removing current)

        for (int j = 0; j < n; j++) {
            int rj = rank[j];

            // Remove current from right side first (so right is strictly k>j)
            right.add(rj, -1);
            remainingRight--;

            long leftLess = left.sum(rj - 1);
            long leftGreater = seenLeft - left.sum(rj); // left elements strictly greater than a[j]

            long rightLess = right.sum(rj - 1);
            long rightGreater = remainingRight - right.sum(rj); // right elements strictly greater than a[j]

            // increasing: (i<j with Ri<Rj) * (k>j with Rk>Rj)
            ans += leftLess * rightGreater;
            // decreasing: (i<j with Ri>Rj) * (k>j with Rk<Rj)
            ans += leftGreater * rightLess;

            // add current to left for next iterations
            left.add(rj, 1);
            seenLeft++;
        }

        return ans;
    }

    // Fast input (handles spaces/newlines)
    static final class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream in) { this.in = in; }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do { c = readByte(); } while (c <= ' ' && c != -1);
            if (c == -1) return Integer.MIN_VALUE;
            int sign = 1;
            if (c == '-') { sign = -1; c = readByte(); }
            int val = 0;
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return val * sign;
        }
    }

    public static void main(String[] args) throws Exception {
//        FastScanner fs = new FastScanner(System.in);
//
//        int n = fs.nextInt();
//        if (n == Integer.MIN_VALUE) {
//            // Demo run if no input provided
//
//        }
        int[] demo = {5, 2, 3, 1, 4};
        System.out.println(countTeams(demo)); // expected 3

//
//        int[] a = new int[n];
//        for (int i = 0; i < n; i++) a[i] = fs.nextInt();
//
//        System.out.println(countTeams(a));
    }
}