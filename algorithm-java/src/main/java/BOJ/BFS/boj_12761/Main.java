package BOJ.BFS.boj_12761;

import java.io.*;
import java.util.*;

public class Main {
    static int a, b, n, m;
    static boolean[] visited = new boolean[100001];

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        a = Integer.parseInt(st.nextToken());
        b = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        System.out.println(bfs());
    }

    static int bfs() {
        Queue<int[]> queue = new LinkedList<>();

        queue.offer(new int[]{n, 0});
        visited[n] = true;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();

            int pos = cur[0];
            int cnt = cur[1];

            if (pos == m) return cnt;

            int[] next = {pos + 1, pos - 1, pos - a, pos - b, pos + a, pos + b, pos * a, pos * b};

            for (int ne : next) {
                if (ne < 0 || ne > 100000) continue;
                if (visited[ne]) continue;

                visited[ne] = true;
                queue.offer(new int[]{ne, cnt + 1});
            }
        }
        return -1;
    }
}
