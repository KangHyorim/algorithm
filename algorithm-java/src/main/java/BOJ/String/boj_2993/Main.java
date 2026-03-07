package BOJ.String.boj_2993;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();

        String answer = null;

        for (int i = 1; i < str.length() - 1; i++) {
            for (int j = i + 1; j < str.length(); j++) {
                String a = new StringBuilder(str.substring(0,i)).reverse().toString();
                String b = new StringBuilder(str.substring(i,j)).reverse().toString();
                String c = new StringBuilder(str.substring(j)).reverse().toString();

                String result = a + b + c;
                if(answer == null || result.compareTo(answer) < 0) answer = result;
            }
        }
        System.out.println(answer);
    }
}
