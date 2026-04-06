package BOJ.Implementation.boj_2304;

import java.io.*;
import java.util.*;

public class Main {
    static int N;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        int[] height = new int[1001];
        int minL = 1001, maxL = 0;

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int L = Integer.parseInt(st.nextToken());   // 왼쪽 면의 위치
            int H = Integer.parseInt(st.nextToken());   // 높이

            height[L] = H;
            minL = Math.min(minL, L);
            maxL = Math.max(maxL, L);
        }

        int[] left = new int[1001];
        int currentLeftMax = 0;
        for (int i = minL; i <= maxL; i++) {
            currentLeftMax = Math.max(currentLeftMax, height[i]);
            left[i] = currentLeftMax;
        }

        int[] right = new int[1001];
        int currentRightMax = 0;
        for (int i = maxL; i >= minL; i--) {
            currentRightMax = Math.max(currentRightMax, height[i]);
            right[i] = currentRightMax;
        }

        int ans = 0;
        for (int i = minL; i <= maxL; i++) {
            ans += Math.min(left[i], right[i]);
        }

        System.out.println(ans);
    }
}
