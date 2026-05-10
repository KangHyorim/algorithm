package SWEA.no_1959;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Solution {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());

            int[] A = new int[N];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) A[i] = Integer.parseInt(st.nextToken());

            int[] B = new int[M];
            st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) B[i] = Integer.parseInt(st.nextToken());

            // 핵심 로직: 항상 A가 짧은 배열이 되도록 설정하여 연산 단순화
            if (N > M) {
                int result = solve(B, A); // M, N 순서 바꿈
                System.out.println("#" + t + " " + result);
            } else {
                int result = solve(A, B);
                System.out.println("#" + t + " " + result);
            }
        }
    }

    /**
     * 짧은 배열(shortArr)을 긴 배열(longArr) 위에서 이동시키며 최댓값 계산
     */
    private static int solve(int[] shortArr, int[] longArr) {
        int max = Integer.MIN_VALUE;
        int sLen = shortArr.length;
        int lLen = longArr.length;

        // 이동 가능한 횟수: (긴 배열 길이 - 짧은 배열 길이)
        for (int i = 0; i <= lLen - sLen; i++) {
            int currentSum = 0;
            for (int j = 0; j < sLen; j++) {
                // 마주보는 숫자끼리 곱하여 합산
                currentSum += shortArr[j] * longArr[i + j];
            }
            // 최댓값 갱신
            if (currentSum > max) {
                max = currentSum;
            }
        }
        return max;
    }
}