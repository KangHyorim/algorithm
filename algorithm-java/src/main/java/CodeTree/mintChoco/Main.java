package CodeTree.mintChoco;

import java.io.*;
import java.util.*;

public class Main {
    static int N, T;
    static int[][] F; // 음식 종류를 비트마스크로 저장 (1: 민트, 2: 초코, 4: 기타 등등)
    static int[][] B; // 각 칸의 '신앙심' (Belief)
    static int[] dx = {-1, 1, 0, 0}; // 방향 정보 (상, 하, 좌, 우)
    static int[] dy = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        F = new int[N][N];
        B = new int[N][N];

        // 1. 초기 맵 세팅: T, C, M 문자를 비트마스크 숫자로 변환해서 저장
        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < N; j++) {
                char ch = str.charAt(j);
                if (ch == 'T') F[i][j] = 1;      // Mint
                else if (ch == 'C') F[i][j] = 2; // Choco
                else F[i][j] = 4;                // Etc
            }
        }

        // 2. 초기 신앙심 입력
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                B[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 3. T일 동안 반복되는 미친 전쟁의 시작
        for (int t = 0; t < T; t++) {
            morning();                   // 아침: 모두의 신앙심이 1씩 증가 (경건한 시간)
            List<int[]> heads = afternoon(); // 점심: 교파 형성 및 교주(Head) 선출, 신앙심 몰아주기
            evening(heads);              // 저녁: 교주들의 포교 활동 (다른 음식 개조하기)
            print();                     // 결과 출력 (매일매일의 전황 보고)
        }
    }

    /**
     * [아침] 평화롭게 모든 칸의 신앙심이 1 상승합니다.
     */
    static void morning() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                B[i][j]++;
            }
        }
    }

    static boolean isRange(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    /**
     * [점심 - BFS] 인접한 같은 음식끼리 그룹을 묶고, 그들 중 '교주(Head)'를 뽑아 신앙심을 몰아줍니다.
     */
    static int[] getHeadAndAdd(boolean[][] visited, int x, int y) {
        Queue<int[]> queue = new LinkedList<>();
        List<int[]> members = new ArrayList<>();

        queue.add(new int[]{x, y});
        visited[x][y] = true;
        members.add(new int[]{x, y});

        // BFS로 같은 음식 종류를 가진 인접 칸들을 싹 긁어모읍니다.
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int cx = curr[0], cy = curr[1];

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];

                if (!isRange(nx, ny) || visited[nx][ny] || F[cx][cy] != F[nx][ny]) continue;

                queue.add(new int[]{nx, ny});
                visited[nx][ny] = true;
                members.add(new int[]{nx, ny});
            }
        }

        members.sort((o1, o2) -> {
            // o1과 o2는 각각 [행, 열] 좌표를 담고 있는 int[] 배열입니다.
            int r1 = o1[0], c1 = o1[1];
            int r2 = o2[0], c2 = o2[1];

            // 1순위: 신앙심(B) 내림차순 (큰 것이 앞으로)
            if (B[r1][c1] != B[r2][c2]) {
                return B[r2][c2] - B[r1][c1]; // o2 - o1은 내림차순
            }

            // 2순위: 행(Row) 오름차순 (작은 것이 앞으로)
            if (r1 != r2) {
                return r1 - r2; // o1 - o2는 오름차순
            }

            // 3순위: 열(Col) 오름차순 (작은 것이 앞으로)
            return c1 - c2;
        });

        // 정렬 후 0번째 인덱스가 가장 우선순위가 높은 '교주(Head)'가 됩니다.
        int[] head = members.get(0);
        int hx = head[0], hy = head[1];

        // 나머지 신도들은 신앙심을 1씩 깎고, 그 모든 정기를 교주에게 몰아줍니다.
        for (int[] m : members) {
            int mx = m[0], my = m[1];
            if (mx == hx && my == hy) continue;
            B[mx][my]--;
        }
        B[hx][hy] += members.size() - 1;

        return new int[]{hx, hy}; // 선출된 교주의 좌표 반환
    }

    static List<int[]> afternoon() {
        boolean[][] visited = new boolean[N][N];
        List<int[]> heads = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!visited[i][j]) {
                    heads.add(getHeadAndAdd(visited, i, j));
                }
            }
        }
        return heads;
    }

    /**
     * [저녁] 선출된 교주들이 자신의 신앙심을 소모하여 직선 방향으로 포교를 나갑니다.
     */
    static void evening(List<int[]> heads) {
        // 교주 활동 순서 정렬: 음식 종류 비트 개수 적은 순 -> 신앙심 높은 순 -> 행 -> 열
        heads.sort((a, b) -> {
            int foodCountA = Integer.bitCount(F[a[0]][a[1]]);
            int foodCountB = Integer.bitCount(F[b[0]][b[1]]);
            if (foodCountA != foodCountB) return foodCountA - foodCountB;

            int beliefA = B[a[0]][a[1]];
            int beliefB = B[b[0]][b[1]];
            if (beliefA != beliefB) return beliefB - beliefA;

            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1];
        });

        boolean[][] defended = new boolean[N][N]; // 하루에 한 번만 공격받도록 방어막 설정

        for (int[] head : heads) {
            int x = head[0], y = head[1];
            if (defended[x][y]) continue; // 이미 오늘 포교당한 칸은 넘어감

            int power = B[x][y] - 1;   // 교주가 쓸 수 있는 포교 에너지
            int dir = B[x][y] % 4;     // 포교 방향 (신앙심 % 4)
            int food = F[x][y];        // 전파할 음식 타입

            B[x][y] = 1; // 교주는 정기를 다 써서 신앙심이 1이 됨

            int cx = x, cy = y;
            while (power > 0) {
                cx += dx[dir];
                cy += dy[dir];
                if (!isRange(cx, cy)) break; // 맵 밖으로 나가면 포교 종료

                if (F[cx][cy] == food) continue; // 이미 우리 교파면 그냥 통과

                defended[cx][cy] = true; // 이 칸은 오늘 포교 완료

                // 에너지가 저항력(상대 신앙심)보다 크면 아예 개조해버림
                if (power > B[cx][cy]) {
                    F[cx][cy] = food;       // 음식 교체
                    power -= (B[cx][cy] + 1); // 에너지 소모
                    B[cx][cy]++;            // 개조된 칸 신앙심 살짝 증가
                } else {
                    // 에너지가 부족하면 비트를 섞어서 혼종(민트초코 등)을 만듦
                    F[cx][cy] |= food;
                    B[cx][cy] += power;
                    break; // 에너지 다 씀
                }
            }
        }
    }

    /**
     * [결과 출력] 각 음식 조합별 신앙심의 총합을 출력합니다.
     */
    static void print() {
        long[] S = new long[8]; // 비트마스크 결과값(0~7)을 담을 배열
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                S[F[i][j]] += B[i][j];
            }
        }

        // 문제 요구사항에 맞는 비트 조합 순서대로 신앙심 합계 출력
        // S[7]은 민트+초코+기타 모두 섞인 혼종의 끝판왕
        System.out.println(S[7] + " " + S[3] + " " + S[5] + " " +
                S[6] + " " + S[4] + " " + S[2] + " " + S[1]);
    }
}