package CodeTree.Taekbae;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] board;
    static List<Box> boxes = new ArrayList<>();
    static StringBuilder sb;

    static class Box implements Comparable<Box> {
        int k, h, w, c, r;

        Box(int k, int h, int w, int c, int r) {
            this.k = k;
            this.h = h;
            this.w = w;
            this.c = c;
            this.r = r;
        }

        @Override
        public int compareTo(Box o) {
            return this.k - o.k;
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

            int r = 1;
            while (true) {
                boolean canFall = true;
                for (int j = c; j < c + w; j++) {
                    int maxRow = r + h;
                    if (maxRow > N || board[maxRow][j] != 0) {
                        canFall = false;
                        break;
                    }
                }
                if (canFall) r++;
                else break;
            }
            b.r = r;

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

    static void leaveLeft() {
        Collections.sort(boxes);

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

    static void leaveRight() {
        Collections.sort(boxes);

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
        for (int i = b.r; i < b.r + b.h; i++) {
            for (int j = b.c; j < b.c + b.w; j++) {
                board[i][j] = b.k;
            }
        }
    }

    static void applyGravity() {
        int[][] newBoard = new int[N + 1][N + 1];
        Collections.sort(boxes, (o1, o2) -> {
            return (o2.r + o2.h) - (o1.r + o1.h);
        });

        for (Box b : boxes) {
            int r = b.r;
            int c = b.c;

            while (true) {
                boolean canFall = true;

                for (int i = c; i < c + b.w; i++) {
                    int bottomRow = r + b.h;
                    if (bottomRow > N || newBoard[bottomRow][i] != 0) {
                        canFall = false;
                        break;
                    }
                }

                if (canFall) r++;
                else break;
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
}