package BOJ.Hash.boj_26069;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        HashSet<String> set = new HashSet<>();
        set.add("ChongChong");
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String A = st.nextToken();
            String B = st.nextToken();

            if(set.contains(A) && !set.contains(B)) set.add(B);
            if(set.contains(B) && !set.contains(A)) set.add(A);
        }
        System.out.println(set.size());
    }
}
