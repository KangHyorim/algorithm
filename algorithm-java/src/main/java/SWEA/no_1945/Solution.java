package SWEA.no_1945;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int i = 1; i <= T; i++) {
            int n = Integer.parseInt(br.readLine());

            int[] primes = {2, 3, 5, 7, 11};
            int[] counts = new int[5];

            for (int j = 0; j < primes.length; j++) {
                while (n % primes[j] == 0) {
                    counts[j]++;
                    n /= primes[j];
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(i);
            for (int count : counts) {
                sb.append(" ").append(count);
            }
            System.out.println(sb.toString());
        }
    }
}
