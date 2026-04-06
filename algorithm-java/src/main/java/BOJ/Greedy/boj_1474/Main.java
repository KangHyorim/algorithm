package BOJ.Greedy.boj_1474;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static String[] words;
    static String answer = null;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        words = new String[N];
        int totalLen = 0;

        for (int i = 0; i < N; i++) {
            words[i] = br.readLine();
            totalLen += words[i].length();
        }

        int totalUnder = M - totalLen;
        int base = totalUnder / (N - 1);
        int remain = totalUnder % (N - 1);

        // N-1개의 간격에 base 또는 base+1을 배치, remain개만 base+1
        // 모든 조합을 탐색
        int[] spaces = new int[N - 1];
        Arrays.fill(spaces, base);

        dfs(spaces, remain, 0);

        System.out.println(answer);
    }

    static void dfs(int[] spaces, int remain, int idx) {
        if (idx == N - 1) {
            if (remain != 0) return;
            String candidate = build(spaces);
            if (answer == null || compare(candidate, answer) < 0) {
                answer = candidate;
            }
            return;
        }

        // 현재 간격에 base 배치
        dfs(spaces, remain, idx + 1);

        // 현재 간격에 base+1 배치
        if (remain > 0) {
            spaces[idx]++;
            dfs(spaces, remain - 1, idx + 1);
            spaces[idx]--;
        }
    }

    static String build(int[] spaces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(words[i]);
            if (i < N - 1) sb.append("_".repeat(spaces[i]));
        }
        return sb.toString();
    }

    // 문제의 사전순 비교: A<...<Z<_<a<...<z
    static int charOrder(char c) {
        if (c >= 'A' && c <= 'Z') return c - 'A';
        if (c == '_') return 26;
        return 27 + (c - 'a');
    }

    static int compare(String a, String b) {
        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            int diff = charOrder(a.charAt(i)) - charOrder(b.charAt(i));
            if (diff != 0) return diff;
        }
        return a.length() - b.length();
    }
}