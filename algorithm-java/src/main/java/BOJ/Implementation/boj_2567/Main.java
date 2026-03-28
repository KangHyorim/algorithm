package BOJ.Implementation.boj_2567;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[][] arr = new int[101][101];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            for (int r = a; r < a + 10; r++) {
                for (int c = b; c < b + 10; c++) {
                    arr[r][c] = 1;
                }
            }
        }

        int perimeter = 0;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};


        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= 100; j++) {
                if (arr[i][j] == 1) {
                    for (int d = 0; d < 4; d++) {
                        int nx = i + dx[d];
                        int ny = j + dy[d];

                        if (arr[nx][ny] == 0) perimeter++;
                    }
                }
            }
        }
        System.out.println(perimeter);
    }
}
