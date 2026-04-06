package CodeTree.AIRobot;

import java.io.*;
import java.util.*;

public class Main {
    static int N, K, L;
    static int[][] map;
    static int[][] vMap;
    static List<Vacuum> vacuums = new ArrayList<>();

    static int[] dr = {0, 1, 0, -1};
    static int[] dc = {-1, 0, 1, 0};

    static class Vacuum implements Comparable<Vacuum> {
        int num;
        int r, c;

        Vacuum(int num, int r, int c) {
            this.num = num;
            this.r = r;
            this.c = c;
        }

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

        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            vMap[r][c] = 101 + i;
            vacuums.add(new Vacuum(vMap[r][c], r, c));
        }

        for (int i = 0; i < L; i++) {
            // System.out.println("L : " + i);
//            printMap();
//            printVMap();
            moveVacuum();
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
        // System.out.println("ANSWER");
        System.out.println(sum);
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
//
//        System.out.println("Spread Dust");
//        printMap();
//        printVMap();
    }

    static void loadDust() {
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (map[i][j] > 0) {
                    map[i][j] += 5;
                }
            }
        }
//        System.out.println("Load Dust");
//        printMap();
//        printVMap();
    }

    static void clean() {
        for (Vacuum v : vacuums) {
            int dir = selectDir(v);
            int r = v.r, c = v.c;

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
//        System.out.println("Clean");
//        printMap();
//        printVMap();
    }

    static void cleanDust(int r, int c) {
        if (r < 1 || r > N || c < 1 || c > N) return;
        if (map[r][c] <= 0) return;
        map[r][c] = Math.max(0, map[r][c] - 20);
        if (map[r][c] < 0) map[r][c] = 0;
    }

    static int selectDir(Vacuum v) {
        int max = Integer.MIN_VALUE;
        int maxDir = -1;
        int r = v.r;
        int c = v.c;

        // getDust 대신 getCleanableDust 사용
        int self = getCleanableDust(r, c);
        int base = self + getCleanableDust(r - 1, c) + getCleanableDust(r + 1, c) + getCleanableDust(r, c + 1) + getCleanableDust(r, c - 1);

        int right = base - getCleanableDust(r, c - 1);
        int down = base - getCleanableDust(r - 1, c);
        int left = base - getCleanableDust(r, c + 1);
        int up = base - getCleanableDust(r + 1, c);

        int[] sums = {right, down, left, up};
        for (int d = 0; d < 4; d++) {
            if (sums[d] > max) {
                max = sums[d];
                maxDir = d;
            }
        }
        return maxDir;
    }

    // [수정]
    static int getCleanableDust(int r, int c) {
        if (r < 1 || c < 1 || r > N || c > N) return 0;
        if (map[r][c] <= 0) return 0; // 벽(-1)이거나 빈칸(0)이면 0
        return Math.min(map[r][c], 20); // 핵심: 한 칸당 최대 20까지만 계산
    }

    static int getDust(int r, int c) {
        if (r < 1 || c < 1 || r > N || c > N) return 0;
        if (map[r][c] < 0) return 0;
        return map[r][c];
    }

    static int[] bfs(Vacuum v) {
        if (map[v.r][v.c] >= 1 && map[v.r][v.c] <= 100) {
            return new int[]{v.r, v.c}; // [수정] 자기 자신이 오염되었다면 이동하지 않음
        }

        int[][] dist = new int[N + 1][N + 1];
        boolean[][] visited = new boolean[N + 1][N + 1];
        List<int[]> candidates = new ArrayList<>();

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{v.r, v.c});
        visited[v.r][v.c] = true;

        int minDist = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] curr = q.poll();

            if (dist[curr[0]][curr[1]] == minDist) break;

            for (int d = 0; d < 4; d++) {
                int nr = curr[0] + dr[d];
                int nc = curr[1] + dc[d];

                if (nr < 1 || nr > N || nc < 1 || nc > N) continue;
                if (visited[nr][nc]) continue;
                if (vMap[nr][nc] > 100 || map[nr][nc] < 0) continue;

                visited[nr][nc] = true;
                dist[nr][nc] = dist[curr[0]][curr[1]] + 1;

                if (map[nr][nc] >= 1 && map[nr][nc] <= 100) {
                    minDist = dist[nr][nc];
                    candidates.add(new int[]{nr, nc});
                }
                q.add(new int[]{nr, nc});
            }
        }
        if (candidates.isEmpty()) return null;

        Collections.sort(candidates, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });

        return candidates.get(0);
    }

    static void moveVacuum() {
        Collections.sort(vacuums);

        for (Vacuum v : vacuums) {
            int[] target = bfs(v);
            if (target == null) continue;

            vMap[v.r][v.c] = 0;
            v.r = target[0];
            v.c = target[1];
            vMap[v.r][v.c] = v.num;
        }
//        System.out.println("Move Vacuum");
//        printMap();
//        printVMap();
    }

    private static void printMap() {
        System.out.println("MAP");
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printVMap() {
        System.out.println("V-MAP");
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                System.out.print(vMap[i][j] + " ");
            }
            System.out.println();
        }
    }
}
