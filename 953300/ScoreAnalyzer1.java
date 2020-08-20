/*
    ScoreAnalyzer1.java
    953300 岡山 紘大
    2020/7/30
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScoreAnalyzer1 {
    HashMap<String, Integer> scoreCount = new HashMap<>();
    Integer studentCount = 0;

    void run(String[] args) throws IOException {
        if (args.length > 1) {
            String probNum = args[0];
            createMap(probNum, new File(args[1]));
            printResult();
        }
    }

    void createMap(String probNum, File thisFile) throws IOException {
        /*
            指定した問題のマップを作成する、問題に参加した生徒の数を数える
        */

        BufferedReader in = new BufferedReader(new FileReader(thisFile));
        String line;
        while ((line = in.readLine()) != null) {
            String[] testInfo = line.split(",");
            if (Objects.equals(testInfo[2], probNum)) {
                scoreCount.put(testInfo[4], scoreCount.getOrDefault(testInfo[4], 0) + 1);
                studentCount += 1;
            }
        }
        in.close();
    }

    void printResult() {
        /*
            結果を出力する
        */

        for (Map.Entry<String, Integer> entry : scoreCount.entrySet()) {
            System.out.printf("%2s: %6.3f (%d/%d)%n", entry.getKey(), (double) entry.getValue() * 100 / studentCount,
                    entry.getValue(), studentCount);
        }
    }

    public static void main(String[] args) throws IOException {
        ScoreAnalyzer1 application = new ScoreAnalyzer1();
        application.run(args);
    }
}