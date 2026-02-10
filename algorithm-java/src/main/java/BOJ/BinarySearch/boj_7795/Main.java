package BOJ.BinarySearch.boj_7795;

import java.io.*;
import java.util.*;

public class Main {
    static int T, N, M;
    static StringTokenizer st;
    static int[] A, B;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());

            A = new int[N];
            B = new int[M];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) A[i] = Integer.parseInt(st.nextToken());
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) B[i] = Integer.parseInt(st.nextToken());

            Arrays.sort(B);
            int sum = 0;
            for (int i = 0; i < N; i++) {
                sum += binarySearch(A[i]);
            }
            System.out.println(sum);
        }
    }

    static int binarySearch(int k) {
        int left = 0;
        int right = M - 1;
        int result = 0;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (B[mid] < k) {
                result = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }
}
