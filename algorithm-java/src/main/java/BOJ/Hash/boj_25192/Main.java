package BOJ.Hash.boj_25192;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        HashSet<String> set = new HashSet<>();
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            if (str.equals("ENTER")){
                cnt += set.size();
                set = new HashSet<>();
                continue;
            }else{
                set.add(str);
            }
        }
        cnt += set.size();
        System.out.println(cnt);
    }
}
