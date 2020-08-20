/*
    probAnalyzer3.java
    953300 岡山 紘大
    2020/8/12
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

public class ScoreAnalyzer3 {
    HashMap<String, StudentScore> scoreMap = new HashMap<>(); //各生徒の成績
    HashMap<String, Stats> statsMap = new HashMap<>(); //統計
    ArrayList<Integer> questions = new ArrayList<>(); //問題の種類

    void run(String[] args) throws IOException {
        if (args.length > 0) {
            createStudentScore(new File(args[0]));
            printResult();
        }
    }

    void createStudentScore(File thisFile) throws IOException {
        /*
            ファイルを読み込みStudentScoreに値を入れる
        */

        BufferedReader in = new BufferedReader(new FileReader(thisFile));
        String line;
        while ((line = in.readLine()) != null) {
            String[] testInfo = line.split(",");
            putStudentScore(testInfo[3], testInfo[2], testInfo[4]);
        }
        in.close();
    }

    void putStudentScore(String id, String question, String value) {
        /*
            生徒のスコアを作成・追加する
            統計のデータを追加
            問題番号の種類を追加する
        */
        if (!this.scoreMap.containsKey(id)) {
            this.scoreMap.put(id, new StudentScore());
        }

        if (!this.questions.contains(Integer.valueOf(question))) {
            this.questions.add(Integer.valueOf(question));
            this.statsMap.put(question, new Stats());
        }

        if (!Objects.equals("", value)) {
            Integer valueInt = Integer.valueOf(value);
            this.scoreMap.get(id).scoreInfo.put(question, valueInt);
            this.scoreMap.get(id).stats.put(valueInt);
            this.statsMap.get(question).put(valueInt);
        }

    }

    void printResult() {
        /*
            結果を出力する
        */
        for (Map.Entry<String, StudentScore> idEntry : this.scoreMap.entrySet()) {
            System.out.printf("%s", idEntry.getKey());
            Collections.sort(this.questions);
            for (Integer question : this.questions) {
                printScore(idEntry.getKey(), question);
            }
            
            if (this.scoreMap.get(idEntry.getKey()).stats.count() > 0) {
                System.out.printf(",%d", this.scoreMap.get(idEntry.getKey()).stats.max());
                System.out.printf(",%d", this.scoreMap.get(idEntry.getKey()).stats.min());
                System.out.printf(",%f", this.scoreMap.get(idEntry.getKey()).stats.average());
            } else {//一問解いていないなら
                System.out.printf(",");
                System.out.printf(",");
                System.out.printf(",");
            }
            System.out.printf("%n");
        }
        printStats();
    }

    void printScore(String id, Integer question) {
        /*
            各生徒の成績を出力
        */

        if (this.scoreMap.get(id).scoreInfo.containsKey(question.toString())) {
            Integer number = this.scoreMap.get(id).scoreInfo.get(question.toString());

            System.out.printf(",%d", number);
        } else {
            System.out.printf(",");
        }
    }

    void printStats() {
        /*
            統計の最大値、最小値、平均を出力
        */
        for (Integer question : this.questions) {
            System.out.printf(",%d", this.statsMap.get(question.toString()).max());
        }
        System.out.println();
        for (Integer question : this.questions) {
            System.out.printf(",%d", this.statsMap.get(question.toString()).min());
        }
        System.out.println();
        for (Integer question : this.questions) {
            System.out.printf(",%f", this.statsMap.get(question.toString()).average());
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        ScoreAnalyzer3 application = new ScoreAnalyzer3();
        application.run(args);
    }
}