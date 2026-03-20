package BOJ.BinarySearch.boj_19637;

import java.io.*;
import java.util.*;

public class Main {
    static String[] names;
    static int[] limits;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        names = new String[N];
        limits = new int[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            names[i] = st.nextToken();
            limits[i] = Integer.parseInt(st.nextToken());
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            int power = Integer.parseInt(br.readLine());
            int idx = binarySearch(power);
            sb.append(names[idx]).append("\n");
        }

        System.out.print(sb);
    }

    static int binarySearch(int target) {
        int left = 0;
        int right = limits.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (limits[mid] >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }
}