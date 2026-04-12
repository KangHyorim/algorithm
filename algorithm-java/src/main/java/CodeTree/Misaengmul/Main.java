package CodeTree.Misaengmul;

import java.io.*;
import java.util.*;

public class Main {
    static int N, Q;
    static int[][] board;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static class Group implements Comparable<Group> {
        int time;
        int size;
        List<int[]> points = new ArrayList<>();

        Group(int time, int size, List<int[]> points) {
            this.time = time;
            this.size = size;
            this.points = points;
        }

        @Override
        public int compareTo(Group g) {
            if (this.size != g.size) return g.size - this.size;
            return this.time - g.time;
        }
    }

    static List<Group> groups = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        board = new int[N][N];

        Q = Integer.parseInt(st.nextToken());
        // 각 실험 결과 기록
        for (int i = 1; i <= Q; i++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());

            // 1. 미생물 투입
            putMicrobe(r1, c1, r2, c2, i);
            // 2. 배양 용기 이동
            moveContainer();
            // 3. 실험 결과 기록
            recordResult();
        }
    }

    private static void putMicrobe(int x1, int y1, int x2, int y2, int num) {
        List<int[]> points = new ArrayList<>();
        Set<Integer> affected = new HashSet<>();

        for (int r = x1; r < x2; r++) {
            for (int c = y1; c < y2; c++) {
                if (board[r][c] > 0) {
                    affected.add(board[r][c]);
                }
                board[r][c] = num;
                points.add(new int[]{r, c});
            }
        }
        groups.add(new Group(num, points.size(), points));

        for (int id : affected) {
            if (!isSplited(id)) {
                removeMicrobe(id);
            }
        }
    }

    private static boolean isSplited(int origin) {
        int startX = -1;
        int startY = -1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == origin) {
                    startY = j;
                    startX = i;
                }
            }
        }

        if (startX == -1) return false;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});

        boolean[][] visited = new boolean[N][N];

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0];
            int y = curr[1];
            visited[x][y] = true;

            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d];
                int ny = y + dy[d];

                if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                if (!visited[nx][ny] && board[nx][ny] == origin) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        List<int[]> p = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!visited[i][j] && board[i][j] == origin) return false;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == origin) p.add(new int[]{i, j});
            }
        }

        for (Group g : groups) {
            if (g.time == origin) {
                g.points = p;
                g.size = p.size();
                break;
            }
        }

        return true;
    }

    private static void removeMicrobe(int num) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == num) board[i][j] = 0;
            }
        }

        groups.removeIf(g -> g.time == num);
    }

    static int[][] newBoard;

    private static void moveContainer() {
        newBoard = new int[N][N];
        Collections.sort(groups);

        // [수정] Iterator를 사용하여 배치에 실패한 미생물을 리스트에서 안전하게 제거
        Iterator<Group> iterator = groups.iterator();
        while (iterator.hasNext()) {
            Group g = iterator.next();
            boolean isPlaced = false; // [수정] 현재 그룹의 배치 여부 플래그

            outer:
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (newBoard[i][j] == 0 && canMove(g.points, i, j)) {
                        moveMicrobe(g.points, i, j, g.time);
                        isPlaced = true; // [수정] 배치 성공
                        break outer;
                    }
                }
            }

            // [수정] 만약 끝까지 자리를 찾지 못해 배치되지 못했다면(파괴됨), groups 리스트에서도 삭제
            if (!isPlaced) {
                iterator.remove();
            }
        }
        board = newBoard;
    }

    private static boolean canMove(List<int[]> points, int x, int y) {
        Collections.sort(points, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });

        int minX = points.get(0)[0];
        int minY = points.get(0)[1];

        for (int[] p : points) {
            int nx = p[0] + x - minX;
            int ny = p[1] + y - minY;
            //System.out.println("nx: " + nx + " ny: " + ny);
            if (nx < 0 || ny < 0 || nx >= N || ny >= N) return false;
            if (newBoard[nx][ny] != 0) return false;
        }
        return true;
    }

    private static void moveMicrobe(List<int[]> points, int x, int y, int num) {
        List<int[]> newP = new ArrayList<>();

        Collections.sort(points, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o1[1] - o2[1];
        });

        int sX = points.get(0)[0];
        int sY = points.get(0)[1];

        // System.out.println("x: " + x + " y: " + y);
        for (int[] p : points) {
            int nx = p[0] + x - sX;
            int ny = p[1] + y - sY;
            //System.out.println("nx: " + nx + " ny: " + ny);

            newBoard[nx][ny] = num;
            newP.add(new int[]{nx, ny});
        }

        for (Group g : groups) {
            if (g.time == num) {
                g.points = newP;
                break;
            }
        }
    }

    private static void recordResult() {
        boolean[][] isAdjacent = new boolean[Q + 1][Q + 1];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) continue;

                for (int d = 0; d < 4; d++) {
                    int nx = i + dx[d];
                    int ny = j + dy[d];

                    if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                    if (board[nx][ny] == 0) continue;

                    if (board[nx][ny] != board[i][j]) {
                        isAdjacent[board[nx][ny]][board[i][j]] = true;
                        isAdjacent[board[i][j]][board[nx][ny]] = true;
                    }
                }
            }
        }

        int sum = 0;
        for (int i = 1; i <= Q; i++) {
            for (int j = i + 1; j <= Q; j++) {
                if (isAdjacent[i][j]) {
                    int sizeI = 0, sizeJ = 0;
                    for (Group g : groups) {
                        if (g.time == i) sizeI = g.size;
                        if (g.time == j) sizeJ = g.size;
                    }
                    sum += sizeI * sizeJ;
                }
            }
        }
        System.out.println(sum);
    }

}

