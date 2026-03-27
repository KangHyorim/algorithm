package BOJ.Simulation.boj_19236;

import java.io.*;
import java.util.*;

public class Main {
    // 공백, 상, 좌상, 좌, 좌하, 하, 우하, 우, 우상
    static int[] dx = {0, 0, -1, -1, -1, 0, 1, 1, 1};
    static int[] dy = {0, -1, -1, 0, 1, 1, 1, 0, -1};

    static Fish[][] fishes = new Fish[4][4];
    static int sharkX = 0;
    static int sharkY = 0;
    static int sharkD;
    static int maxAte = 0;
    static int[][] fishPos = new int[17][2];

    private static class Fish {
        int num, dir;

        Fish(int num, int dir) {
            this.num = num;
            this.dir = dir;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        for (int i = 0; i < 4; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 4; j++) {
                int num = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken());
                fishes[i][j] = new Fish(num, dir);
                fishPos[num][0] = i;
                fishPos[num][1] = j;
            }
        }
        Fish first = fishes[0][0];
        sharkD = first.dir;
        sharkX = 0;
        sharkY = 0;

        fishPos[first.num][0] = -1;
        fishPos[first.num][1] = -1;
        fishes[0][0] = null;

        dfs(0, 0, sharkD, first.num);

        System.out.print(maxAte);
    }

    static void dfs(int sx, int sy, int sd, int sum) {
        maxAte = Math.max(maxAte, sum);

        Fish[][] copyFish = new Fish[4][4];
        int[][] copyPos = new int[17][2];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (fishes[i][j] != null) {
                    copyFish[i][j] = new Fish(fishes[i][j].num, fishes[i][j].dir);
                }
            }
        }
        for (int i = 1; i <= 16; i++) {
            copyPos[i][0] = fishPos[i][0];
            copyPos[i][1] = fishPos[i][1];
        }

        sharkX = sx;
        sharkY = sy;

        moveFishes();
        moveShark(sx, sy, sd, sum);

        // 전체 복구
        fishes = copyFish;
        fishPos = copyPos;
    }

    static void moveFishes() {
        for (int i = 1; i <= 16; i++) {
            moveFish(i);
        }
    }

    static void moveFish(int num) {
        int row = fishPos[num][0];
        int col = fishPos[num][1];
        if (row == -1) return;

        Fish current = fishes[row][col];
        int dir = current.dir;

        for (int i = 0; i < 8; i++) {
            int nextDir = (dir + i - 1) % 8 + 1;
            int nr = row + dy[nextDir];
            int nc = col + dx[nextDir];

            if (canMoveFish(nc, nr)) {
                current.dir = nextDir;

                // swap
                Fish target = fishes[nr][nc];
                if (target != null) {
                    fishPos[target.num][0] = row;
                    fishPos[target.num][1] = col;
                }

                fishPos[num][0] = nr;
                fishPos[num][1] = nc;

                fishes[nr][nc] = current;
                fishes[row][col] = target;

                return;
            }
        }
    }

    static boolean canMoveFish(int x, int y) {
        if (x == sharkX && y == sharkY) return false;
        if (x < 0 || x >= 4 || y < 0 || y >= 4) return false;

        return true;
    }

    static void moveShark(int sx, int sy, int sd, int sum) {
        for (int step = 1; step <= 3; step++) {
            int nx = sx + dx[sd] * step; // 열 변화
            int ny = sy + dy[sd] * step; // 행 변화

            // 1. 범위 체크
            if (nx < 0 || nx >= 4 || ny < 0 || ny >= 4) break;
            // 2. 빈칸 체크
            if (fishes[ny][nx] == null) continue;

            Fish target = fishes[ny][nx];
            int targetNum = target.num;

            // [먹기]
            fishes[ny][nx] = null;
            fishPos[targetNum][0] = -1;
            fishPos[targetNum][1] = -1;

            // [다음 탐색]
            dfs(nx, ny, target.dir, sum + targetNum);

            // [복구]
            fishes[ny][nx] = target;
            fishPos[targetNum][0] = ny; // 행
            fishPos[targetNum][1] = nx; // 열
        }
    }
}
