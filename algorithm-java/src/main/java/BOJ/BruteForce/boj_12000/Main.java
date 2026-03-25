package BOJ.BruteForce.boj_12000;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];
        int answer = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        for (int i = 0; i < n; i++) {
            int dist = 0;

            for (int k = 0; k < n; k++) {
                int idx = (i + k) % n;
                dist += arr[idx] * k;
            }
            answer = Math.min(answer, dist);
        }

        System.out.println(answer);
    }
}
