package BOJ.Sort.boj_5800;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int K = Integer.parseInt(br.readLine());
        for (int i = 0; i < K; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int[] arr = new int[N];

            for (int j = 0; j < N; j++) {
                arr[j] = Integer.parseInt(st.nextToken());
            }
            Arrays.sort(arr);

            int max = arr[N - 1];
            int min = arr[0];
            int gap = 0;

            for (int x = N - 1; x > 0; x--) {
                gap = Math.max(gap, arr[x] - arr[x - 1]);
            }

            System.out.println("Class " + (i + 1));
            System.out.println("Max " + max + ", Min " + min + ", Largest gap " + gap);
        }
    }
}
