package BOJ.Queue.boj_1715;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        if (n == 1) {
            System.out.println(0);
            return;
        }

        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            pq.add(Long.parseLong(br.readLine()));
        }

        long sum = 0;
        while (pq.size() > 1) {
            long a = pq.poll();
            long b = pq.poll();

            sum += (a + b);
            pq.add(a + b);
        }

        System.out.println(sum);

    }
}
