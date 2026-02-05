package BOJ.BFS.boj_18126;

import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<int[]>[] graph;
    static boolean[] visited;
    static long maxDist = 0;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        graph = new ArrayList[N + 1];
        visited = new boolean[N + 1];
        for (int i = 1; i <= N; i++) graph[i] = new ArrayList<>();

        StringTokenizer st;
        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            graph[a].add(new int[]{b, c});
            graph[b].add(new int[]{a, c});
        }
        bfs();
        System.out.println(maxDist);
    }

    static void bfs() {
        Queue<long[]> queue = new LinkedList<>();
        queue.offer(new long[]{1, 0});
        visited[1] = true;

        while (!queue.isEmpty()) {
            long[] cur = queue.poll();

            int node = (int) cur[0];
            long dist = cur[1];

            maxDist = Math.max(maxDist, dist);

            for (int[] next : graph[node]) {
                int nextNode = next[0];
                int nextDist = next[1];

                if (visited[nextNode]) continue;

                visited[nextNode] = true;
                queue.offer(new long[]{nextNode, dist + nextDist});
            }
        }

    }
}
