package BOJ.DP.boj_1890;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] board;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        board = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) board[i][j] = Integer.parseInt(st.nextToken());
        }

        long[][] dp = new long[N][N];
        dp[0][0] = 1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (dp[i][j] == 0) continue;
                if (i == N - 1 && j == N - 1) continue;

                int jump = board[i][j];
                if (i + jump < N) {
                    dp[i + jump][j] += dp[i][j];
                }
                if (j + jump < N) {
                    dp[i][j + jump] += dp[i][j];
                }
            }
        }
        System.out.println(dp[N - 1][N - 1]);
    }
}
