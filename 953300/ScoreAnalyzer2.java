/*
    probAnalyzer2.java
    953300 岡山 紘大
    2020/7/30
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScoreAnalyzer2 {
    HashMap<String, HashMap<String, Integer>> probMap = new HashMap<>(); //成績表
    ArrayList<Integer> scores = new ArrayList<>(); //スコアの種類。時間に間に合わない=-1

    void run(String[] args) throws IOException {
        if (args.length > 0) {
            createMap(new File(args[0]));
            printResult();
        }
    }

    void createMap(File thisFile) throws IOException {
        /*
            指定した問題のマップを作成する
            スコアの種類を保存する
        */

        BufferedReader in = new BufferedReader(new FileReader(thisFile));
        String line;
        while ((line = in.readLine()) != null) {
            String[] testInfo = line.split(",");
            if (!probMap.containsKey(testInfo[2])) {
                probMap.put(testInfo[2], new HashMap<String, Integer>());
            }
            probMap.get(testInfo[2]).put(testInfo[4], probMap.get(testInfo[2]).getOrDefault(testInfo[4], 0) + 1);

            Integer score = Objects.equals(testInfo[4], "") ? -1 : Integer.valueOf(testInfo[4]);
            if (!scores.contains(score)) {
                scores.add(score);
            }
        }
        in.close();
    }

    void printResult() {
        /*
            結果を出力する
        */

        Collections.sort(scores);
        for (Integer num : scores) {
            System.out.printf(",%s", num == -1 ? "" : num.toString());
        }
        System.out.println();
        for (Map.Entry<String, HashMap<String, Integer>> entry : probMap.entrySet()) {
            System.out.printf("%s", entry.getKey());
            printRate(entry.getValue());
            System.out.println();
        }
    }

    void printRate(HashMap<String, Integer> scoreMap) {
        /*
            問題のスコアの割合を出力する
        */

        Integer studentsNum = countStudentsNum(scoreMap);
        for (Integer num : scores) {
            String str = num == -1 ? "" : num.toString();
            if (scoreMap.containsKey(str)) {
                System.out.printf(",%6.3f", (double) scoreMap.get(str) * 100 / studentsNum);
            } else {
                System.out.printf(",");
            }
        }
    }

    Integer countStudentsNum(HashMap<String, Integer> scoreMap) {
        /*
            その問題に参加した生徒の数を数える
        */

        Integer count = 0;
        for (Integer num : scoreMap.values()) {
            count += num;
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        ScoreAnalyzer2 application = new ScoreAnalyzer2();
        application.run(args);
    }
}