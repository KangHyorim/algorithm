package BOJ.Implementation.boj_2304;

import java.io.*;
import java.util.*;

public class Main {
    static int N;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        int[] height = new int[1001];

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int L = Integer.parseInt(st.nextToken());   // 왼쪽 면의 위치
            int H = Integer.parseInt(st.nextToken());   // 높이

            height[L] = H;
        }

        int[] left = new int[1001];
        for (int i = 1; i <= 1000; i++) {
            left[i] = Math.max(left[i - 1], height[i]);
        }

        int[] right = new int[1001];
        for (int i = 999; i >= 0; i--) {
            right[i] = Math.max(right[i + 1], height[i]);
        }

        int ans = 0;
        for (int i = 0; i <= 1000; i++) {
            ans += Math.min(left[i], right[i]);
        }

        System.out.println(ans);
    }
}
