/*
    probAnalyzer4.java
    953300 岡山 紘大
    2020/8/12
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ScoreAnalyzer4 {
    HashMap<String, StudentScore> scoreMap = new HashMap<>(); //各生徒の成績
    HashMap<String, Stats> statsMap = new HashMap<>(); //統計
    ArrayList<Integer> questions = new ArrayList<>(); //問題の種類

    void run(String[] args) throws IOException {
        if (args.length > 0) {
            createStudentScore(new File(args[0]));
            printResult();

            Integer height = this.questions.size() * 3; //高さ
            Integer width = this.scoreMap.size() * 3; //幅

            drawHeatMap(width, height);
            outputImage(width, height);
        }
    }

    void drawHeatMap(Integer width, Integer height) {
        /*
            ヒートマップを描写する
        */
        EZ.initialize(width, height);
        Integer countY = 0;
        for (Integer question : this.questions) {
            Integer countX = 0;
            for (StudentScore score : this.scoreMap.values()) {
                Color color = calculatePixelColor(score.scoreInfo.get(question.toString()),
                        this.statsMap.get(question.toString()).max());
                EZ.addRectangle(countX * 3, countY * 3, 3, 3, color, true);
                countX += 1;
            }
            countY += 1;
        }
    }

    Color calculatePixelColor(Integer point, Integer maxScore) {
        /*
            画素の計算をする
        */
        if (point == null) {
            return new Color(0xff, 0xff, 0xff, 0xff); // 白の透明色
        }
        Double color = Double.valueOf(255.0 * point / maxScore);
        return new Color(color.intValue(), 0, 0); // 赤の場合
    }

    void outputImage(Integer width, Integer height) throws IOException {
        /*
            画像ファイルを作成する
        */
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        EZ.app.paintComponent(g); // image に図形を描画する．
        // image を "png" フォーマットで "heatmap.png" に出力する．
        ImageIO.write(image, "png", new File("heatmap.png"));
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
        ScoreAnalyzer4 application = new ScoreAnalyzer4();
        application.run(args);
    }
}