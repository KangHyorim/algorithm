package BOJ.String.boj_1343;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();

        StringBuilder sb = new StringBuilder();

        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'X') {
                count++;
            } else {
                if (count % 2 != 0) {
                    System.out.println(-1);
                    return;
                }

                while (count >= 4) {
                    sb.append("AAAA");
                    count -= 4;
                }
                if (count == 2) {
                    sb.append("BB");
                }

                count = 0;
                sb.append(".");
            }
        }

        if (count % 2 != 0) {
            System.out.println(-1);
            return;
        }

        while (count >= 4) {
            sb.append("AAAA");
            count -= 4;
        }
        if (count == 2) {
            sb.append("BB");
        }

        System.out.println(sb.toString());
    }
}