package BOJ.Deque.boj_24511;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());

        int[] A = new int[N];
        int[] B = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            A[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            B[i] = Integer.parseInt(st.nextToken());
        }

        Deque<Integer> dq = new ArrayDeque<>();

        for (int i = 0; i < N; i++) {
            if (A[i] == 0) {
                dq.addLast(B[i]);
            }
        }

        int M = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            int x = Integer.parseInt(st.nextToken());
            dq.addFirst(x);
            sb.append(dq.pollLast()).append(" ");
        }

        System.out.println(sb);
    }
}