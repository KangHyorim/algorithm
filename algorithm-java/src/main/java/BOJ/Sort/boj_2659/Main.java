package BOJ.Sort.boj_2659;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());

        int target = getClockNum(a, b, c, d);

        int cnt = 0;
        for (int i = 1111; i <= target; i++) {
            if (isClockNum(i)) cnt++;
        }
        System.out.println(cnt);
    }

    private static int getClockNum(int a, int b, int c, int d) {
        int min = Integer.MAX_VALUE;
        int[] nums = {a, b, c, d};

        for (int i = 0; i < 4; i++) {
            int current = nums[i] * 1000 + nums[(i + 1) % 4] * 100 + nums[(i + 2) % 4] * 10 + nums[(i + 3) % 4];
            if (current < min) min = current;
        }
        return min;
    }

    private static boolean isClockNum(int n) {
        int a = n / 1000;
        int b = (n / 100) % 10;
        int c = (n / 10) % 10;
        int d = n % 10;

        if (a == 0 || b == 0 || c == 0 || d == 0) return false;

        int clock = getClockNum(a, b, c, d);

        return clock == n;
    }
}
