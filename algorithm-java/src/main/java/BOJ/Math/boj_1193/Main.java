package BOJ.Math.boj_1193;

import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int X = Integer.parseInt(br.readLine());

        int diagonal = 1;
        int sum = 0;

        while (sum + diagonal < X) {
            sum += diagonal;
            diagonal++;
        }

        int idx = X - sum;

        int numerator, denominator;

        if (diagonal % 2 == 0) {
            numerator = idx;
            denominator = diagonal - idx + 1;
        } else {
            numerator = diagonal - idx + 1;
            denominator = idx;
        }

        System.out.println(numerator + "/" + denominator);
    }
}
