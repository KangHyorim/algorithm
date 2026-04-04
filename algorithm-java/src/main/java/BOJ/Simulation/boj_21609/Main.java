package BOJ.Simulation.boj_21609;

import java.io.*;
import java.util.*;

class Group implements Comparable<Group> {
    int rainbowCnt, row, col, cnt;
    List<int[]> points = new LinkedList<>();

    @Override
    public int compareTo(Group o) {
        if (this.cnt != o.cnt) return o.cnt - this.cnt;
        if (this.rainbowCnt != o.rainbowCnt) return o.rainbowCnt - this.rainbowCnt;
        if (this.row != o.row) return o.row - this.row;
        return o.col - this.col;
    }

    Group(int row, int col, int cnt, int rainbowCnt, List<int[]> points) {
        this.row = row;
        this.col = col;
        this.cnt = cnt;
        this.rainbowCnt = rainbowCnt;
        this.points = points;
    }
}

public class Main {
    static int N, M;
    static int[][] board;
    static boolean[][] visited;
    static List<Group> groups = new LinkedList<>();
    static int score = 0;

    // 상 -> 하 -> 좌 -> 우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};


    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        board = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        while (true) {
            Group group = findLargestGroup();
            if (group == null) break;
            score += (group.cnt * group.cnt);

            removeGroup(group);
            applyGravity();
            rotate();
            applyGravity();
        }
        System.out.println(score);
    }

    static void removeGroup(Group g) {
        List<int[]> points = g.points;

        for (int[] p : points) {
            int x = p[0];
            int y = p[1];

            board[x][y] = -2;
        }

//        System.out.println("after removed");
//        printBoard();
    }

    static void applyGravity() {
        for (int j = 0; j < N; j++) {
            int pointer = N - 1;

            for (int i = N - 1; i >= 0; i--) {
                if (board[i][j] == -1) {
                    pointer = i - 1;

                } else if (board[i][j] >= 0) {
                    int value = board[i][j];

                    board[i][j] = -2;
                    board[pointer][j] = value;

                    pointer--;
                }
            }
        }

//        System.out.println("after gravity");
//        printBoard();
    }

    static void rotate() {
        int[][] b = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                b[N - 1 - j][i] = board[i][j];
            }
        }
        board = b;
    }

    static Group findLargestGroup() {
        // 그룹 만들기
        groups = new LinkedList<>();
        visited = new boolean[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] > 0 && !visited[i][j]) {
                    bfs(i, j);
                }
            }
        }
        if (groups.isEmpty()) return null;

        Collections.sort(groups);
        Group large = groups.get(0);
        return large;
    }

    static void bfs(int x, int y) {
        Queue<int[]> q = new LinkedList<>();
        List<int[]> points = new LinkedList<>();
        List<int[]> rainbowList = new LinkedList<>();

        int color = board[x][y];

        q.add(new int[]{x, y});
        visited[x][y] = true;
        points.add(new int[]{x, y});

        int cnt = 1;
        int rainbowCnt = 0;


        while (!q.isEmpty()) {
            int[] cur = q.poll();

            for (int d = 0; d < 4; d++) {
                int nx = cur[0] + dx[d];
                int ny = cur[1] + dy[d];

                if (nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
                if (visited[nx][ny] || board[nx][ny] == -1) continue;

                // 같은 색 or 무지개만 가능
                if (board[nx][ny] != 0 && board[nx][ny] != color) continue;

                visited[nx][ny] = true;
                q.add(new int[]{nx, ny});
                points.add(new int[]{nx, ny});
                cnt++;

                if (board[nx][ny] == 0) {
                    rainbowCnt++;
                    rainbowList.add(new int[]{nx, ny});
                }
            }
        }

        // 무지개 visited 해제
        for (int[] r : rainbowList) {
            visited[r[0]][r[1]] = false;
        }

        // 그룹 조건: 크기 2 이상
        if (cnt >= 2) {
            groups.add(new Group(x, y, cnt, rainbowCnt, points));
        }
    }

    static void printBoard() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

}