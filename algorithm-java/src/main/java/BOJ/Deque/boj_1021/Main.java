package BOJ.Deque.boj_1021;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        LinkedList<Integer> deq = new LinkedList<>();
        for (int i = 1; i <= N; i++) {
            deq.add(i);
        }

        st = new StringTokenizer(br.readLine());
        int[] targets = new int[M];
        for (int i = 0; i < M; i++) targets[i] = Integer.parseInt(st.nextToken());

        int cnt = 0;

        for(int target : targets){
            int targetIdx = deq.indexOf(target);
            int halfIdx = deq.size() / 2;

            if(targetIdx <= halfIdx){
                for(int i = 0; i < targetIdx; i++){
                    deq.addLast(deq.pollFirst());
                    cnt++;
                }
            }else{
                for(int i = 0; i < deq.size() - targetIdx; i++){
                    deq.addFirst(deq.pollLast());
                    cnt++;
                }
            }
            deq.pollFirst();
        }
        System.out.println(cnt);
    }
}
