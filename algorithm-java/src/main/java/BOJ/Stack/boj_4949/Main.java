package BOJ.Stack.boj_4949;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String str = br.readLine();
            if (str.equals(".")) break;

            Stack<Character> stack = new Stack<>();
            boolean flag = true;

            for (char c : str.toCharArray()) {
                if (c == '(' || c == '[') stack.push(c);
                else if (c == ')') {
                    if (stack.isEmpty() || stack.peek() != '(') {
                        flag = false;
                        break;
                    }
                    stack.pop();
                } else if (c == ']') {
                    if (stack.isEmpty() || stack.peek() != '[') {
                        flag = false;
                        break;
                    }
                    stack.pop();
                }
            }
            if (!stack.isEmpty()) flag = false;
            System.out.println(flag ? "yes" : "no");
        }
    }
}
