package BOJ.Simulation.boj_1268;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[][] classes = new int[N][5];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                classes[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int maxCount = -1;
        int leader = 1;

        for (int i = 0; i < N; i++) {
            int count = 0;

            for (int j = 0; j < N; j++) {
                if (i == j) continue;

                for (int grade = 0; grade < 5; grade++) {
                    if (classes[i][grade] == classes[j][grade]) {
                        count++;
                        break;
                    }
                }
            }

            if (count > maxCount) {
                maxCount = count;
                leader = i + 1;
            }
        }

        System.out.println(leader);
    }
}