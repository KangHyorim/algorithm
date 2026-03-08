package BOJ.Stack.boj_28278;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());

            if (op == 1) {
                int x = Integer.parseInt(st.nextToken());
                stack.push(x);
            } else if (op == 2) {
                if (!stack.isEmpty()){
                    System.out.println(stack.pop());
                }else{
                    System.out.println("-1");
                }
            } else if (op == 3) {
                System.out.println(stack.size());
            } else if (op == 4) {
                if(!stack.isEmpty()) System.out.println("0");
                else System.out.println("1");
            } else if (op == 5) {
                if(!stack.isEmpty()) System.out.println(stack.peek());
                else System.out.println("-1");
            }
        }
    }
}
