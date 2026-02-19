package BOJ.Bitmask.boj_15787;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[] train = new int[N + 1];

        for (int t = 0; t < M; t++) {
            st = new StringTokenizer(br.readLine());
            int order = Integer.parseInt(st.nextToken());
            int i = Integer.parseInt(st.nextToken());

            if (order == 1) {
                int x = Integer.parseInt(st.nextToken());
                train[i] |= 1 << (x - 1);
            } else if (order == 2) {
                int x = Integer.parseInt(st.nextToken());
                train[i] &= ~(1 << (x - 1));
            } else if (order == 3) {
                train[i] <<= 1;
                train[i] &= (1 << 20) - 1;
            } else {
                train[i] >>= 1;
            }
        }
        HashSet<Integer> set = new HashSet<>();
        for (int i = 1; i <= N; i++) set.add(train[i]);

        System.out.println(set.size());
    }
}
