package BOJ.Hash.boj_16165;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        Map<String, List<String>> teamMembers = new HashMap<>();
        Map<String, String> memberTeam = new HashMap<>();

        for (int i = 0; i < N; i++) {
            String group = br.readLine();

            List<String> list = new ArrayList<>();

            int n = Integer.parseInt(br.readLine());
            for (int j = 0; j < n; j++) {
                String member = br.readLine();
                list.add(member);
                memberTeam.put(member, group);
            }
            Collections.sort(list);
            teamMembers.put(group, list);
        }

        for(int i = 0; i < M; i++){
            String name = br.readLine();
            int type = Integer.parseInt(br.readLine());

            if(type == 0){
                for(String mem : teamMembers.get(name)){
                    System.out.println(mem);
                }
            }else{
                System.out.println(memberTeam.get(name));
            }
        }
    }
}
