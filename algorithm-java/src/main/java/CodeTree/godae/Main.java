package CodeTree.godae;

import java.util.*;
import java.io.*;

public class Main {
    static int K, M, bestScore;
    static Queue<Integer> wall = new ArrayDeque<>();
    static boolean[][] visit;
    static int[][] map = new int[5][5];
    static List<int[]> list, temp;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            wall.offer(Integer.parseInt(st.nextToken()));
        }

        for (int t = 0; t < K; t++) {
            simulation();
        }
    }

    private static void simulation() {
        bestScore = 0;
        list = new ArrayList<>();
        temp = new ArrayList<>();
        // 가장 점수를 많이 획득할 수 있는 지점을 구하자
        int[] expect = getExpectPosition();

        // 점수를 획득할 수 없던 경우 바로 종료
        if (list.size() == 0)
            return;

        // rotate(x,y,degree) 특정 좌표를 기준으로 구한 각도로 회전
        map = rotate(expect[0], expect[1], expect[2]);

        // 지우기
        for (int[] pos : list)
            removeItem(pos[0], pos[1]);

        // 채우기
        fillItem();

        // 추가 점수 획득
        while (true) {
            visit = new boolean[5][5];
            int count = 0;
            temp.clear();
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    count += bfs(i, j, map);
                }
            }
            if (count == 0)
                break;

            visit = new boolean[5][5];
            for (int[] pos : temp)
                removeItem(pos[0], pos[1]);
            fillItem();
            bestScore += count;
        }
        System.out.print(bestScore + " ");
    }

    private static int[] getExpectPosition() {
        int max = 0;
        int rx = -1;
        int ry = -1;
        int rd = -1;

        // 우선순위를 고려하여 위치 찾기.
        for (int d = 1; d < 4; ++d) {
            for (int j = 1; j < 4; ++j) {
                for (int i = 1; i < 4; ++i) {
                    int[][] rotateMap = rotate(i, j, d);
                    visit = new boolean[5][5];
                    temp.clear();
                    int score = 0;
                    for (int r = 0; r < 3; ++r) {
                        for (int c = 0; c < 3; ++c) {
                            if (!visit[r + i - 1][c + j - 1])
                                score += bfs(r + i - 1, c + j - 1, rotateMap);
                        }
                    }
                    if (score > max) {
                        list.clear();
                        list.addAll(temp);
                        rx = i;
                        ry = j;
                        rd = d;
                        max = score;
                    }
                }
            }
        }
        bestScore += max;
        return new int[]{rx, ry, rd};
    }

    private static void removeItem(int x, int y) {
        visit = new boolean[5][5];
        visit[x][y] = true;
        int base = map[x][y];
        map[x][y] = 0;
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{x, y});

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            for (int d = 0; d < 4; ++d) {
                int nx = pos[0] + dx[d];
                int ny = pos[1] + dy[d];
                if (!isInRange(nx, ny))
                    continue;
                if (!visit[nx][ny] && map[nx][ny] == base) {
                    visit[nx][ny] = true;
                    map[nx][ny] = 0;
                    q.add(new int[]{nx, ny});
                }
            }
        }
    }

    private static void fillItem() {
        for (int j = 0; j < 5; ++j) {
            for (int i = 4; i >= 0; --i)
                if (map[i][j] == 0)
                    map[i][j] = wall.poll();
        }
    }

    private static int bfs(int x, int y, int[][] rotateArr) {
        int count = 1;

        visit[x][y] = true;
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{x, y});

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            for (int d = 0; d < 4; ++d) {
                int nx = pos[0] + dx[d];
                int ny = pos[1] + dy[d];
                if (!isInRange(nx, ny))
                    continue;
                if (!visit[nx][ny] && rotateArr[nx][ny] == rotateArr[x][y]) {
                    count++;
                    visit[nx][ny] = true;
                    q.add(new int[]{nx, ny});
                }
            }
        }

        if (count > 2) {
            temp.add(new int[]{x, y});
            return count;
        }
        return 0;
    }

    public static int[][] rotate(int x, int y, int d) {
        int[][] copy = new int[3][3];
        int[][] rotateArr = new int[5][5];

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j)
                rotateArr[i][j] = map[i][j];
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j)
                if (d == 1) // 90도 회전
                    copy[i][j] = map[3 - j + x - 2][i + y - 1];
                else if (d == 2) // 180도 회전
                    copy[i][j] = map[3 - i + x - 2][3 - j + y - 2];
                else // 270도 회전
                    copy[i][j] = map[j + x - 1][3 - i + y - 2];
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j)
                rotateArr[i + x - 1][j + y - 1] = copy[i][j];
        }
        return rotateArr;
    }

    public static boolean isInRange(int x, int y) {
        if (0 <= x && x < 5 && 0 <= y && y < 5)
            return true;
        return false;
    }
}