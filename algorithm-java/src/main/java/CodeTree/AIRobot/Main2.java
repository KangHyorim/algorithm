package CodeTree.AIRobot;

import java.io.*;
import java.util.*;

public class Main2 {
    static int N, K, L;
    static int[][] map;
    static int[][] vMap;
    static List<Vacuum> vacuums = new ArrayList<>();

    // 오른쪽, 아래쪽, 왼쪽, 위쪽
    static int[] dr = {0, 1, 0, -1};
    static int[] dc = {1, 0, -1, 0};

    static class Vacuum implements Comparable<Vacuum> {
        int num, r, c;

        Vacuum(int num, int r, int c) {
            this.num = num;
            this.r = r;
            this.c = c;
        }

        @Override
        public int compareTo(Vacuum o) {
            return this.num - o.num;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        map = new int[N + 1][N + 1];
        vMap = new int[N + 1][N + 1];

        // 물건  -1, 먼지는 1 ~ 100 사이
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 1; i <= K; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            vacuums.add(new Vacuum(i, r, c));
            vMap[r][c] = i;
        }

        for (int i = 0; i < L; i++) {
            moveVacuums();
            clean();
            loadDust();
            spreadDust();
            printResult();
        }
    }


    static void printResult() {
        int sum = 0;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (map[i][j] > 0) {
                    sum += map[i][j];
                }
            }
        }
        System.out.println(sum);
    }

    static int getDust(int r, int c) {
        if (r < 1 || c < 1 || r > N || c > N) return 0;
        if (map[r][c] < 0) return 0;
        return map[r][c];
    }

    static void spreadDust() {
        List<int[]> candidates = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (map[i][j] == 0) {
                    int sum = getDust(i - 1, j) + getDust(i + 1, j) + getDust(i, j - 1) + getDust(i, j + 1);
                    candidates.add(new int[]{i, j, sum / 10});
                }
            }
        }

        for (int[] ca : candidates) {
            int r = ca[0];
            int c = ca[1];
            map[r][c] += ca[2];
            if (map[r][c] > 100) map[r][c] = 100;
        }
    }

    static void loadDust() {
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (map[i][j] > 0) {
                    map[i][j] += 5;
                }
            }
        }
    }

    static int getCleanableDust(int r, int c) {
        if (r < 1 || c < 1 || r > N || c > N) return 0;
        if (map[r][c] < 0) return 0;
        if (map[r][c] >= 20) return 20;
        return map[r][c];
    }

    static void clean() {
        for (Vacuum v : vacuums) {
            int dir = -1;
            int r = v.r;
            int c = v.c;

            int base = getCleanableDust(r, c) + getCleanableDust(r - 1, c) + getCleanableDust(r + 1, c)
                    + getCleanableDust(r, c - 1) + getCleanableDust(r, c + 1);

            int right = base - getCleanableDust(r, c - 1);
            int down = base - getCleanableDust(r - 1, c);
            int left = base - getCleanableDust(r, c + 1);
            int up = base - getCleanableDust(r + 1, c);

            int[] sums = {right, down, left, up};

            int max = -1;
            for (int d = 0; d < 4; d++) {
                if (sums[d] > max) {
                    max = sums[d];
                    dir = d;
                }
            }
            int[][] targets;
            if (dir == 0) // 오른쪽
                targets = new int[][]{{r, c}, {r - 1, c}, {r + 1, c}, {r, c + 1}};
            else if (dir == 1) // 아래
                targets = new int[][]{{r, c}, {r, c - 1}, {r, c + 1}, {r + 1, c}};
            else if (dir == 2) // 왼쪽
                targets = new int[][]{{r, c}, {r - 1, c}, {r + 1, c}, {r, c - 1}};
            else // 위
                targets = new int[][]{{r, c}, {r, c - 1}, {r, c + 1}, {r - 1, c}};

            for (int[] t : targets) {
                cleanDust(t[0], t[1]);
            }
        }
    }

    static void cleanDust(int r, int c) {
        if (r < 1 || r > N || c < 1 || c > N) return;
        if (map[r][c] < 0) return;
        map[r][c] = Math.max(0, map[r][c] - 20);
    }

    static void moveVacuums() {
        Collections.sort(vacuums);

        for (Vacuum v : vacuums) {
            int[] target = findTarget(v);
            if (target == null) continue;

            vMap[v.r][v.c] = 0;
            v.r = target[0];
            v.c = target[1];
            vMap[v.r][v.c] = v.num;
        }
    }

    static int[] findTarget(Vacuum v) {
        if (map[v.r][v.c] >= 1 && map[v.r][v.c] <= 100) return new int[]{v.r, v.c};

        int[][] dist = new int[N + 1][N + 1];
        List<int[]> candidates = new ArrayList<>();
        boolean[][] visited = new boolean[N + 1][N + 1];
        Queue<int[]> q = new LinkedList<>();

        q.add(new int[]{v.r, v.c});
        visited[v.r][v.c] = true;
        int minDist = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];

            if (dist[r][c] ==  minDist) break;

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr < 1 || nr > N || nc < 1 || nc > N) continue;
                if (visited[nr][nc] || map[nr][nc] < 0 || vMap[nr][nc] > 0) continue;

                visited[nr][nc] = true;
                dist[nr][nc] = dist[r][c] + 1;

                if (map[nr][nc] >= 1 && map[nr][nc] <= 100) {
                    minDist = dist[nr][nc];
                    candidates.add(new int[]{nr, nc});
                }
                q.add(new int[]{nr, nc});
            }
        }

        if (candidates.isEmpty() || candidates.size() == 0) return null;
        Collections.sort(candidates, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });
        return candidates.get(0);
    }
}
