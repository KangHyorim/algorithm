package BOJ.BruteForce.boj_10655;

import java.io.*;
import java.util.*;

public class Main {
    static int[] x;
    static int[] y;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        x = new int[n];
        y = new int[n];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            x[i] = Integer.parseInt(st.nextToken());
            y[i] = Integer.parseInt(st.nextToken());
        }

        int total = 0;
        for (int i = 0; i < n - 1; i++) {
            total += dist(i, i + 1);
        }

        int maxSaved = 0;
        for (int i = 1; i < n - 1; i++) {
            int saved = dist(i - 1, i) + dist(i, i + 1) - dist(i - 1, i + 1);
            maxSaved = Math.max(maxSaved, saved);
        }
        System.out.println(total - maxSaved);
    }

    private static int dist(int a, int b) {
        return Math.abs(x[a] - x[b]) + Math.abs(y[a] - y[b]);
    }
}
