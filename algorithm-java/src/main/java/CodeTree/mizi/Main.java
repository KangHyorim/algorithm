package CodeTree.mizi;

import java.util.*;
import java.io.*;

public class Main {
    static int N, M, F;
    static int[][] map;
    static int[][][] cube;
    static int currentC = -1, currentR = -1;
    static int timeC = -1, timeR = -1;
    static int[][] contamination;
    static int turn = 0;

    // 동, 서, 남, 북
    static int[] dr = {0, 0, 1, -1};
    static int[] dc = {1, -1, 0, 0};

    static class Point {
        int f, r, c, t;

        Point(int f, int r, int c, int t) {
            this.f = f;
            this.r = r;
            this.c = c;
            this.t = t;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());   // 미지의 공간 : N * N
        M = Integer.parseInt(st.nextToken());   // 시간의 벽 : M * M
        F = Integer.parseInt(st.nextToken());   // 시간 이상 현상 개수

        map = new int[N][N];
        cube = new int[5][M][M];
        contamination = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == 3 && timeR == -1) {
                    timeR = i;
                    timeC = j;
                }
            }
        }

        // 동, 서, 남, 북, 위
        for (int d = 0; d < 5; d++) {
            for (int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < M; j++) {
                    cube[d][i][j] = Integer.parseInt(st.nextToken());

                    if (cube[d][i][j] == 2) {
                        currentR = i;
                        currentC = j;
                    }
                }
            }
        }

        for (int i = 0; i < N; i++) Arrays.fill(contamination[i], Integer.MAX_VALUE);

        for (int i = 0; i < F; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            int time = 0;
            int currR = r, currC = c;

            while (currR >= 0 && currR < N && currC >= 0 && currC < N) {
                if (map[currR][currC] == 1 || map[currR][currC] == 3 || map[currR][currC] == 4) break;

                if (time < contamination[currR][currC]) contamination[currR][currC] = time;

                currR += dr[d];
                currC += dc[d];
                time += v;
            }
        }

        System.out.println(bfs());
    }

    static int bfs() {
        Queue<Point> q = new LinkedList();
        boolean[][] vMap = new boolean[N][N];
        boolean[][][] vCube = new boolean[5][M][M];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                if (cube[4][i][j] == 2) {
                    q.add(new Point(4, i, j, 0));
                    vCube[4][i][j] = true;
                }
            }
        }

        while (!q.isEmpty()) {
            Point curr = q.poll();

            if (curr.f == 5 && map[curr.r][curr.c] == 4) return curr.t;

            for (int d = 0; d < 4; d++) {
                int nt = curr.t + 1;

                // 정육면체에서 이동
                if (curr.f < 5) {
                    int nr = curr.r + dr[d];
                    int nc = curr.c + dc[d];
                    int nf = curr.f;

                    // 같은 면인 경우
                    if (nr >= 0 && nr < M && nc >= 0 && nc < M) {
                        // 장애물 없고 방문한 곳이 아닌 경우
                        if (cube[nf][nr][nc] != 1 && !vCube[nf][nr][nc]) {
                            vCube[nf][nr][nc] = true;
                            q.add(new Point(nf, nr, nc, nt));
                        }
                    } else {    // 다른 면으로 넘어가는 경우
                        int nnr = -1, nnc = -1;

                        if (curr.f == 0) {
                            if (nr < 0) {
                                nf = 4;
                                nnr = M - 1 - curr.c;
                                nnc = M - 1;
                            } else if (nr >= M) {
                                nf = 5;
                                nnr = timeR + M - 1 - curr.c;
                                nnc = timeC + M;
                            } else if (nc < 0) {
                                nf = 2;
                                nnr = curr.r;
                                nnc = M - 1;
                            } else if (nc >= M) {
                                nf = 3;
                                nnr = curr.r;
                                nnc = 0;
                            }
                        } else if (curr.f == 1) { // 현재 서쪽 면에 있을 때
                            if (nr < 0) {
                                // 위쪽 경계 벗어남 → 윗면(4)으로
                                nf = 4;
                                nnr = curr.c;
                                nnc = 0;
                            } else if (nr >= M) {
                                // 아래쪽 경계 벗어남 → 바닥(5)으로
                                nf = 5;
                                nnr = timeR + curr.c;
                                nnc = timeC - 1;
                            } else if (nc < 0) {
                                // 왼쪽 경계 벗어남 → 북면(3)으로
                                nf = 3;
                                nnr = curr.r;
                                nnc = M - 1;
                            } else if (nc >= M) {
                                // 오른쪽 경계 벗어남 → 남면(2)으로
                                nf = 2;
                                nnr = curr.r;
                                nnc = 0;
                            }
                        } else if (curr.f == 2) { // 현재 남쪽 면에 있을 때
                            if (nr < 0) {
                                // 위쪽 경계 벗어남 → 윗면(4)으로
                                // 남면의 위쪽은 윗면의 아래쪽 행에 연결
                                nf = 4;
                                nnr = M - 1;
                                nnc = curr.c;
                            } else if (nr >= M) {
                                // 아래쪽 경계 벗어남 → 바닥(5)으로
                                // 남면 아래 = 바닥에서 시간의벽 아래쪽
                                nf = 5;
                                nnr = timeR + M;
                                nnc = timeC + curr.c;
                            } else if (nc < 0) {
                                // 왼쪽 경계 벗어남 → 서면(1)으로
                                nf = 1;
                                nnr = curr.r;
                                nnc = M - 1;
                            } else if (nc >= M) {
                                // 오른쪽 경계 벗어남 → 동면(0)으로
                                nf = 0;
                                nnr = curr.r;
                                nnc = 0;
                            }
                        } else if (curr.f == 3) { // 현재 북쪽 면에 있을 때
                            if (nr < 0) {
                                // 위쪽 경계 벗어남 → 윗면(4)으로
                                // 북면의 위쪽은 윗면의 위쪽 행에 연결 (좌우 반전)
                                nf = 4;
                                nnr = 0;
                                nnc = M - 1 - curr.c;
                            } else if (nr >= M) {
                                // 아래쪽 경계 벗어남 → 바닥(5)으로
                                nf = 5;
                                nnr = timeR - 1;
                                nnc = timeC + M - 1 - curr.c;
                            } else if (nc < 0) {
                                // 왼쪽 경계 벗어남 → 동면(0)으로
                                nf = 0;
                                nnr = curr.r;
                                nnc = M - 1;
                            } else if (nc >= M) {
                                // 오른쪽 경계 벗어남 → 서면(1)으로
                                nf = 1;
                                nnr = curr.r;
                                nnc = 0;
                            }
                        } else if (curr.f == 4) { // 현재 윗면에 있을 때
                            if (nr < 0) {
                                // 위쪽 경계 벗어남 → 북면(3)으로
                                // 윗면 위쪽 = 북면의 위쪽 행 (좌우 반전)
                                nf = 3;
                                nnr = 0;
                                nnc = M - 1 - curr.c;
                            } else if (nr >= M) {
                                // 아래쪽 경계 벗어남 → 남면(2)으로
                                // 윗면 아래쪽 = 남면의 위쪽 행
                                nf = 2;
                                nnr = 0;
                                nnc = curr.c;
                            } else if (nc < 0) {
                                // 왼쪽 경계 벗어남 → 서면(1)으로
                                // 윗면 왼쪽 = 서면의 위쪽 행
                                nf = 1;
                                nnr = 0;
                                nnc = curr.r;
                            } else if (nc >= M) {
                                // 오른쪽 경계 벗어남 → 동면(0)으로
                                // 윗면 오른쪽 = 동면의 위쪽 행 (상하 반전)
                                nf = 0;
                                nnr = 0;
                                nnc = M - 1 - curr.r;
                            }
                        }

                        // 바닥 평면으로 내려간 경우
                        if (nf == 5) {
                            if (nnr >= 0 && nnr < N && nnc >= 0 && nnc < N) {
                                if (map[nnr][nnc] != 1 && map[nnr][nnc] != 3) {
                                    if (nt < contamination[nnr][nnc] && !vMap[nnr][nnc]) {
                                        vMap[nnr][nnc] = true;
                                        q.add(new Point(5, nnr, nnc, nt));
                                    }
                                }
                            }
                        } else {  // 다른 큐브 면으로 넘어간 경우
                            if (cube[nf][nnr][nnc] != 1 && !vCube[nf][nnr][nnc]) {
                                vCube[nf][nnr][nnc] = true;
                                q.add(new Point(nf, nnr, nnc, nt));
                            }
                        }
                    }
                } else {  // 바닥에서 이동
                    int nr = curr.r + dr[d];
                    int nc = curr.c + dc[d];

                    if (nr >= 0 && nr < N && nc >= 0 && nc < N) {
                        if (map[nr][nc] != 1 && map[nr][nc] != 3) {
                            if (nt < contamination[nr][nc] && !vMap[nr][nc]) {
                                vMap[nr][nc] = true;
                                q.add(new Point(5, nr, nc, nt));
                            }
                        }
                    }
                }
            }

        }
        return -1;
    }
}