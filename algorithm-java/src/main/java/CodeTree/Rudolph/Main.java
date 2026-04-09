package CodeTree.Rudolph;

import java.io.*;
import java.util.*;


public class Main {
    static int N, M, P, C, D;
    static int rR = -1, rC = -1;
    static int turn = 0;
    static List<Santa> santas = new ArrayList<>();

    static int[] dr = {0, 0, -1, 1, -1, -1, 1, 1};
    static int[] dc = {-1, 1, 0, 0, -1, 1, -1, 1};

    static class Santa implements Comparable<Santa> {
        int p, r, c, fallenTurn;
        boolean isFallen;
        boolean isOut;
        int score;

        Santa(int p, int r, int c) {
            this.p = p;
            this.r = r;
            this.c = c;
            this.isFallen = false;
            this.isOut = false;
            this.fallenTurn = -1;
            this.score = 0;
        }

        @Override
        public int compareTo(Santa s) {
            return this.p - s.p;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        rR = Integer.parseInt(st.nextToken());
        rC = Integer.parseInt(st.nextToken());

        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());
            int sP = Integer.parseInt(st.nextToken());
            int sR = Integer.parseInt(st.nextToken());
            int sC = Integer.parseInt(st.nextToken());
            santas.add(new Santa(sP, sR, sC));
        }

        for (int i = 1; i <= M; i++) {
            if (santas.stream().allMatch(s -> s.isOut)) break;
            turn++;
            play();
        }

        Collections.sort(santas);
        StringBuilder sb = new StringBuilder();
        for (Santa s : santas) sb.append(s.score).append(" ");
        System.out.println(sb.toString().trim());
    }

    static void play() {
        awake();
        moveR();
        moveS();
        addScore();
    }

    static void addScore() {
        for (Santa s : santas) {
            if (!s.isOut) s.score++;
        }
    }

    static void awake() {
        for (Santa s : santas) {
            if (s.isFallen && (s.fallenTurn + 1 < turn)) {
                s.isFallen = false;
            }
        }
    }

    static void moveR() {
        Santa selected = selectSanta();
        if (selected == null) return;

        int bestDist = Integer.MAX_VALUE;
        int bestR = rR, bestC = rC;
        int dir = -1;

        for (int d = 0; d < 8; d++) {
            int nr = rR + dr[d];
            int nc = rC + dc[d];
            int dist = getDist(selected.r, selected.c, nr, nc);
            if (dist < bestDist) {
                bestDist = dist;
                bestR = nr;
                bestC = nc;
                dir = d;
            }
        }
        rR = bestR;
        rC = bestC;

        for (Santa s : santas) {
            if (s.isOut) continue;
            if (isCollision(rR, rC, s.r, s.c)) {
                s.isFallen = true;
                s.fallenTurn = turn;
                s.score += C;
                s.r += dr[dir] * C;
                s.c += dc[dir] * C;

                if (!isInRange(s.r, s.c)) {
                    s.isOut = true;
                } else {
                    interact(s, dr[dir], dc[dir]);
                }
            }
        }
    }

    static void interact(Santa s, int dirR, int dirC) {
        for (Santa other : santas) {
            if (other == s || other.isOut) continue;
            if (isCollision(s.r, s.c, other.r, other.c)) {
                other.r += dirR;
                other.c += dirC;
                if (!isInRange(other.r, other.c)) {
                    other.isOut = true;
                } else {
                    interact(other, dirR, dirC);
                }
            }
        }
    }

    static Santa selectSanta() {
        Santa selected = null;
        int minDist = Integer.MAX_VALUE;

        Collections.sort(santas);
        for (Santa s : santas) {
            if (s.isOut) continue;

            int distS = getDist(s.r, s.c, rR, rC);
            if (selected == null || distS < minDist) {
                selected = s;
                minDist = distS;
            } else if (distS == minDist) {
                if (s.r > selected.r || (s.r == selected.r && s.c > selected.c)) {
                    selected = s;
                }
            }
        }
        return selected;
    }

    static void moveS() {
        Collections.sort(santas);

        int[] sdr = {-1, 0, 1, 0};
        int[] sdc = {0, 1, 0, -1};

        for (Santa s : santas) {
            if (s.isOut || s.isFallen) continue;

            int curDist = getDist(s.r, s.c, rR, rC);
            int bestDist = curDist;
            int bestR = s.r, bestC = s.c;
            int dir = -1;

            for (int d = 0; d < 4; d++) {
                int nr = s.r + sdr[d];
                int nc = s.c + sdc[d];
                if (!isInRange(nr, nc)) continue;

                // 다른 산타가 있는 칸으로는 이동 불가
                boolean blocked = false;
                for (Santa other : santas) {
                    if (other == s || other.isOut) continue;
                    if (other.r == nr && other.c == nc) { blocked = true; break; }
                }
                if (blocked) continue;

                int dist = getDist(nr, nc, rR, rC);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestR = nr;
                    bestC = nc;
                    dir = d;
                }
            }

            // 가까워지는 방향이 없으면 이동 안 함
            if (dir == -1) continue;

            s.r = bestR;
            s.c = bestC;

            if (isCollision(rR, rC, s.r, s.c)) {
                s.isFallen = true;
                s.fallenTurn = turn;
                s.score += D;
                // 이동해온 반대 방향으로 밀려남
                s.r -= sdr[dir] * D;
                s.c -= sdc[dir] * D;

                if (!isInRange(s.r, s.c)) {
                    s.isOut = true;
                } else {
                    interact(s, -sdr[dir], -sdc[dir]);
                }
            }
        }
    }

    static boolean isCollision(int r1, int c1, int r2, int c2) {
        return r1 == r2 && c1 == c2;
    }

    static int getDist(int r1, int c1, int r2, int c2) {
        return (r1 - r2) * (r1 - r2) + (c1 - c2) * (c1 - c2);
    }

    static boolean isInRange(int r, int c) {
        return r > 0 && r <= N && c > 0 && c <= N;
    }
}