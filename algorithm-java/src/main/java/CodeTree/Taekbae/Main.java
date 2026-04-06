package CodeTree.Taekbae;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] board;
    static List<Box> boxes = new ArrayList<>();
    static StringBuilder sb;

    static class Box {
        int k, h, w, c, r;

        Box(int k, int h, int w, int c, int r) {
            this.k = k;
            this.h = h;
            this.w = w;
            this.c = c;
            this.r = r;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        board = new int[N + 1][N + 1];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int k = Integer.parseInt(st.nextToken());   // 택배 번호
            int h = Integer.parseInt(st.nextToken());   // 세로 길이
            int w = Integer.parseInt(st.nextToken());   // 가로 길이
            int c = Integer.parseInt(st.nextToken());   // 좌측 좌표

            Box b = new Box(k, h, w, c, 1);

            // [수정] 1. 박스 투입 시 즉시 바닥이나 다른 박스 위까지 떨어지도록 시뮬레이션 적용
            int r = 1;
            while (true) {
                boolean canFall = true;
                for (int j = c; j < c + w; j++) {
                    int nextRow = r + h;
                    if (nextRow > N || board[nextRow][j] != 0) {
                        canFall = false;
                        break;
                    }
                }
                if (canFall) {
                    r++;
                } else {
                    break;
                }
            }
            b.r = r; // 최종 낙하 위치 저장

            boxes.add(b);
            putBox(b);
        }

        sb = new StringBuilder();

        while (!boxes.isEmpty()) {
            leaveLeft();
            leaveRight();
        }
        System.out.println(sb);
    }

    static boolean canMoveLeft(Box b) {
        removeBox(b);
        int c = b.c;
        while (c > 1) {
            boolean canMove = true;
            for (int i = b.r; i < b.r + b.h; i++) {
                if (board[i][c - 1] != 0) {
                    canMove = false;
                    break;
                }
            }
            if (canMove) c--;
            else break;
        }
        putBox(b);
        return c == 1;
    }

    static boolean canRemoveRight(Box b) {
        removeBox(b);
        int c = b.c;
        while (c + b.w - 1 < N) {
            boolean canMove = true;
            for (int i = b.r; i < b.r + b.h; i++) {
                if (board[i][c + b.w] != 0) {
                    canMove = false;
                    break;
                }
            }
            if (canMove) c++;
            else break;
        }
        putBox(b);
        return c + b.w - 1 == N;
    }

    static void leaveLeft() {
        Collections.sort(boxes, (o1, o2) -> {
            return o1.k - o2.k;
        });

        for (Box b : boxes) {
            if (canMoveLeft(b)) {
                removeBox(b);
                boxes.remove(b);
                sb.append(b.k).append("\n");
                break;
            }
        }
        applyGravity();
    }

    static void leaveRight() {
        Collections.sort(boxes, (o1, o2) -> {
            return o1.k - o2.k;
        });
        for (Box b : boxes) {
            if (canRemoveRight(b)) {
                removeBox(b);
                boxes.remove(b);
                sb.append(b.k).append("\n");
                break;
            }
        }
        applyGravity();
    }

    static void removeBox(Box b) {
        for (int i = b.r; i < b.r + b.h; i++) {
            for (int j = b.c; j < b.c + b.w; j++) {
                board[i][j] = 0;
            }
        }
    }

    static void putBox(Box b) {
        int k = b.k;
        int w = b.w;
        int h = b.h;
        int c = b.c;
        int r = b.r;

        // [수정] i = 1 이 아니라 박스의 현재 위치인 i = r 부터 그리도록 수정 (기존의 가장 큰 버그)
        for (int i = r; i < r + h; i++) {
            for (int j = c; j < c + w; j++) {
                board[i][j] = k;
            }
        }
    }

    static void applyGravity() {
        int[][] newBoard = new int[N + 1][N + 1];

        // [수정] 2. r(상단)이 아니라 박스의 하단(r + h) 기준으로 내림차순 정렬하여 아래쪽 박스부터 중력 적용
        boxes.sort((a, b) -> (b.r + b.h) - (a.r + a.h));

        for (Box b : boxes) {
            int r = b.r;
            int c = b.c;

            while (true) {
                boolean canFall = true;

                for (int j = c; j < c + b.w; j++) {
                    int nextRow = r + b.h;
                    if (nextRow > N || newBoard[nextRow][j] != 0) {
                        canFall = false;
                        break;
                    }
                }

                if (canFall) {
                    r++;
                } else {
                    break;
                }
            }

            b.r = r;

            for (int i = r; i < r + b.h; i++) {
                for (int j = b.c; j < b.c + b.w; j++) {
                    newBoard[i][j] = b.k;
                }
            }
        }
        board = newBoard;
    }

    static void printBoard() {
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}