package CodeTree.medusa;

import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
    static int[][] map;
    static List<Warrior> warriors = new ArrayList<>();
    static int sr, sc, er, ec;
    static int mr, mc;
    static List<int[]> path = new ArrayList<>();
    static boolean[][] isStone;
    static int warriorMoveCnt, stonedWarrior, attackingWarrior;

    // 상하좌우 순서
    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static class Warrior {
        int r, c;
        boolean isStone;

        Warrior(int r, int c) {
            this.r = r;
            this.c = c;
            this.isStone = false;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][N];

        st = new StringTokenizer(br.readLine());
        // 메두사 집 정보
        sr = Integer.parseInt(st.nextToken());
        sc = Integer.parseInt(st.nextToken());
        // 공원 위치 정보
        er = Integer.parseInt(st.nextToken());
        ec = Integer.parseInt(st.nextToken());

        // 메두사 위치
        mr = sr;
        mc = sc;

        // 전사들의 좌표
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= M; i++) {
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            warriors.add(new Warrior(r, c));
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        if (!findPath()) {
            System.out.println("-1");
            return;
        }
        path.remove(0);

        while (true) {
            play();
            System.out.println(warriorMoveCnt + " " + stonedWarrior + " " + attackingWarrior);
        }
    }

    static void play() {
        moveMedusa();
        if (mr == er && mc == ec) {
            System.out.println("0");
            System.exit(0);
        }
        see();
        moveWarriors();
        recoverWarriors();
    }

    static void recoverWarriors() {
        for (Warrior w : warriors) {
            w.isStone = false;
        }
    }

    static void moveWarriors() {
        warriorMoveCnt = 0;
        attackingWarrior = 0;

        // 첫번째 이동
        Iterator<Warrior> it = warriors.iterator();
        while (it.hasNext()) {
            Warrior w = it.next();

            if (w.isStone) continue;
            int curDist = Math.abs(w.r - mr) + Math.abs(w.c - mc);
            boolean moved1st = false;

            for (int i = 0; i < 4; i++) {
                int nr = w.r + dr[i];
                int nc = w.c + dc[i];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N) {
                    if (isStone != null && isStone[nr][nc]) continue;

                    int nextDist = Math.abs(nr - mr) + Math.abs(nc - mc);
                    if (nextDist < curDist) {
                        w.r = nr;
                        w.c = nc;
                        warriorMoveCnt++;
                        curDist = nextDist; // 2차 이동을 위해 갱신
                        moved1st = true;
                        break;
                    }
                }
            }

            if (moved1st && w.r == mr && w.c == mc) {
                attackingWarrior++;
                it.remove();
                continue; // 소멸했으므로 2차 이동 생략
            }

            // 두 번째 이동
            int[] dr2 = {0, 0, -1, 1};
            int[] dc2 = {-1, 1, 0, 0};
            boolean moved2nd = false;
            for (int i = 0; i < 4; i++) {
                int nr = w.r + dr2[i];
                int nc = w.c + dc2[i];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N) {
                    if (isStone != null && isStone[nr][nc]) continue;

                    int nextDist = Math.abs(nr - mr) + Math.abs(nc - mc);
                    if (nextDist < curDist) {
                        w.r = nr;
                        w.c = nc;
                        warriorMoveCnt++;
                        moved2nd = true;
                        break;
                    }
                }
            }

            // 2차 이동 직후 메두사와 같은 칸에 도달했다면 공격 후 소멸
            if (moved2nd && w.r == mr && w.c == mc) {
                attackingWarrior++;
                it.remove();
            }
        }
    }

    static void see() {
        isStone = new boolean[N][N];
        int maxWatched = -1;

        for (int d = 0; d < 4; d++) {
            boolean[][] isVisible = getVisibleMap(d);
            int cnt = countVisibleWarriors(isVisible);

            if (cnt > maxWatched) {
                maxWatched = cnt;
                isStone = isVisible;
            }
        }
        makeStone(isStone);
    }

    static void makeStone(boolean[][] isStone) {
        stonedWarrior = 0;
        if (isStone == null) return;

        for (Warrior w : warriors) {
            if (isStone[w.r][w.c]) {
                w.isStone = true;
                stonedWarrior++;
            }
        }
    }

    static int countVisibleWarriors(boolean[][] isVisible) {
        int cnt = 0;
        for (Warrior w : warriors) {
            if (isVisible[w.r][w.c]) cnt++;
        }
        return cnt;
    }

    static boolean[][] getVisibleMap(int d) {
        boolean[][] isVisible = new boolean[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (r == mr && c == mc) continue;

                boolean inCone = false;
                if (d == 0) inCone = r < mr && (mr - r) >= Math.abs(c - mc);      // 상
                else if (d == 1) inCone = r > mr && (r - mr) >= Math.abs(c - mc); // 하
                else if (d == 2) inCone = c < mc && (mc - c) >= Math.abs(r - mr); // 좌
                else if (d == 3) inCone = c > mc && (c - mc) >= Math.abs(r - mr); // 우

                if (inCone) isVisible[r][c] = true;
            }
        }

        isVisible = applyShadow(d, isVisible);

        return isVisible;
    }

    static boolean[][] applyShadow(int d, boolean[][] isVisible) {
        List<Warrior> inConeWarriors = new ArrayList<>();
        for (Warrior w : warriors) {
            if (isVisible[w.r][w.c]) {
                inConeWarriors.add(w);
            }
        }

        if (d == 0) inConeWarriors.sort((a, b) -> Integer.compare(b.r, a.r));      // 상: 행 내림차순
        else if (d == 1) inConeWarriors.sort((a, b) -> Integer.compare(a.r, b.r)); // 하: 행 오름차순
        else if (d == 2) inConeWarriors.sort((a, b) -> Integer.compare(b.c, a.c)); // 좌: 열 내림차순
        else if (d == 3) inConeWarriors.sort((a, b) -> Integer.compare(a.c, b.c)); // 우: 열 오름차순

        boolean[][] isShadow = new boolean[N][N];

        for (Warrior w : inConeWarriors) {
            // 이미 다른 전사에 의해 만들어진 그림자 안에 있다면 새로운 그림자를 만들지 않음
            if (isShadow[w.r][w.c]) continue;

            int wr = w.r;
            int wc = w.c;

            // 8방향 상대 위치에 따른 그림자 범위 확산 (거리 차이 k만큼만 확산되도록 수정)
            if (d == 0) { // 상
                for (int r = wr - 1; r >= 0; r--) {
                    int k = wr - r; // 전사로부터 떨어진 세로 거리
                    if (wc == mc) isShadow[r][wc] = true;
                    else if (wc < mc) {
                        for (int c = wc - k; c <= wc; c++) if (c >= 0) isShadow[r][c] = true;
                    } else {
                        for (int c = wc; c <= wc + k; c++) if (c < N) isShadow[r][c] = true;
                    }
                }
            } else if (d == 1) { // 하
                for (int r = wr + 1; r < N; r++) {
                    int k = r - wr;
                    if (wc == mc) isShadow[r][wc] = true;
                    else if (wc < mc) {
                        for (int c = wc - k; c <= wc; c++) if (c >= 0) isShadow[r][c] = true;
                    } else {
                        for (int c = wc; c <= wc + k; c++) if (c < N) isShadow[r][c] = true;
                    }
                }
            } else if (d == 2) { // 좌
                for (int c = wc - 1; c >= 0; c--) {
                    int k = wc - c; // 전사로부터 떨어진 가로 거리
                    if (wr == mr) isShadow[wr][c] = true;
                    else if (wr < mr) {
                        for (int r = wr - k; r <= wr; r++) if (r >= 0) isShadow[r][c] = true;
                    } else {
                        for (int r = wr; r <= wr + k; r++) if (r < N) isShadow[r][c] = true;
                    }
                }
            } else if (d == 3) { // 우
                for (int c = wc + 1; c < N; c++) {
                    int k = c - wc;
                    if (wr == mr) isShadow[wr][c] = true;
                    else if (wr < mr) {
                        for (int r = wr - k; r <= wr; r++) if (r >= 0) isShadow[r][c] = true;
                    } else {
                        for (int r = wr; r <= wr + k; r++) if (r < N) isShadow[r][c] = true;
                    }
                }
            }
        }

        // 최종적으로 시야 배열(isVisible)에서 그림자 영역(isShadow)을 지움
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (isShadow[r][c]) {
                    isVisible[r][c] = false;
                }
            }
        }

        return isVisible;
    }


    static void moveMedusa() {
        mr = path.get(0)[0];
        mc = path.get(0)[1];
        path.remove(0);

        warriors.removeIf(w -> w.r == mr && w.c == mc);
    }

    static boolean findPath() {
        int[][] dist = new int[N][N];
        for (int i = 0; i < N; i++) Arrays.fill(dist[i], -1);

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{er, ec});
        dist[er][ec] = 0;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];

            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N && map[nr][nc] == 0 && dist[nr][nc] == -1) {
                    dist[nr][nc] = dist[r][c] + 1;
                    q.add(new int[]{nr, nc});
                }
            }
        }
        if (dist[sr][sc] == -1) return false;

        int curR = sr;
        int curC = sc;

        while (!(curR == er && curC == ec)) {
            path.add(new int[]{curR, curC});

            for (int i = 0; i < 4; i++) {
                int nr = curR + dr[i];
                int nc = curC + dc[i];

                if (nr >= 0 && nr < N && nc >= 0 && nc < N && dist[nr][nc] == dist[curR][curC] - 1) {
                    curR = nr;
                    curC = nc;
                    break;
                }
            }
        }
        path.add(new int[]{er, ec});
        return true;
    }
}
