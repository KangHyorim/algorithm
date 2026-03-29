package BOJ.Implementation.boj_10709;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int h = Integer.parseInt(st.nextToken());
        int w = Integer.parseInt(st.nextToken());

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < h; i++) {
            String line = br.readLine();
            int cloudPos = -1;

            for (int j = 0; j < w; j++) {
                char current = line.charAt(j);

                if (current == 'c') {
                    cloudPos = 0;
                } else if (cloudPos != -1) {
                    cloudPos++;
                }
                sb.append(cloudPos).append(" ");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }
}
