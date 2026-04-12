package BOJ.Greedy.boj_17615;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        String str = br.readLine();
        int ans = Integer.MAX_VALUE;

        // 1. 빨간 공을 왼쪽으로 모으기
        ans = Math.min(ans, getMoveCount(N, str, 'R', true));
        // 2. 빨간 공을 오른쪽으로 모으기
        ans = Math.min(ans, getMoveCount(N, str, 'R', false));
        // 3. 파란 공을 왼쪽으로 모으기
        ans = Math.min(ans, getMoveCount(N, str, 'B', true));
        // 4. 파란 공을 오른쪽으로 모으기
        ans = Math.min(ans, getMoveCount(N, str, 'B', false));

        System.out.println(ans);
    }

    private static int getMoveCount(int N, String s, char targetColor, boolean toLeft) {
        int count = 0;
        boolean skipSide = true;

        if (toLeft) {
            for (int i = 0; i < N; i++) {
                if (s.charAt(i) == targetColor) {
                    if (skipSide) continue;
                    count++;
                } else {
                    skipSide = false;
                }
            }
        } else {
            for (int i = N - 1; i >= 0; i--) {
                if (s.charAt(i) == targetColor) {
                    if (skipSide) continue;
                    count++;
                } else {
                    skipSide = false;
                }
            }
        }
        return count;
    }
}
