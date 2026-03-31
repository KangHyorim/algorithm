package BOJ.BFS.boj_21736;

import java.io.*;
import java.util.*;

public class Main {
    static int[] dx = {0, 0, -1, 1};
    static int[] dy = {-1, 1, 0, 0};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        char[][] arr = new char[n][m];
        boolean[][] visited = new boolean[n][m];

        int ix = -1;
        int iy = -1;

        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            for (int j = 0; j < m; j++) {
                arr[i][j] = line.charAt(j);

                if (arr[i][j] == 'I') {
                    ix = i;
                    iy = j;
                }
            }
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{ix, iy});
        visited[ix][iy] = true;

        int cnt = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];

                if (nx < 0 || ny < 0 || nx >= n || ny >= m || visited[nx][ny] || arr[nx][ny] == 'X') continue;

                if (arr[nx][ny] == 'P') {
                    cnt++;
                }
                visited[nx][ny] = true;
                queue.add(new int[]{nx, ny});
            }
        }

        if (cnt == 0) {
            System.out.println("TT");
            return;
        }

        System.out.println(cnt);

    }
}
