/*
    probAnalyzer5.java
    953300 岡山 紘大
    2020/8/13
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
import java.util.Objects;

import javax.imageio.ImageIO;

public class ScoreAnalyzer5 {
    HashMap<String, StudentScore5> scoreMap = new HashMap<>(); //各生徒の成績
    HashMap<String, Stats> statsMap = new HashMap<>(); //統計
    ArrayList<Integer> questions = new ArrayList<>(); //問題の種類

    void run(String[] args) throws IOException {
        Arguments arguments = new Arguments();
        ArrayList<StudentScore5> list = new ArrayList<>();
        if (args.length > 0) {
            arguments.parse(args);
            if (arguments.help) {
                printHelp();
                return;
            }

            createStudentScore(new File(arguments.args.get(0)));
            if (!arguments.sort.equals("")) {
                list = sortedStudentList(this.scoreMap, arguments.sort);
            } else {
                list = new ArrayList<>(this.scoreMap.values());
            }
            if (!arguments.heatmap.equals("")) {

            }
            Collections.sort(this.questions);

            printResult(list);

            Integer height = this.questions.size() * 3; //高さ
            Integer width = this.scoreMap.size() * 3; //幅
            drawHeatMap(width, height);
            outputImage(width, height, arguments.dest);
        }
    }

    void printHelp() {
        /*
            Helpを出力する
        */
        System.out.println("java ScoreAnalyzer5 [OPTIONS] <FILENAME.CSV>");
        System.out.println("OPTIONS");
        System.out.println("    --help           このメッセージを表示して終了する");
        System.out.println("    --dest <DEST>    ヒートマップの出力先を指定する．");
        System.out.println("    --sort <ITEM>    指定された項目の昇順でソートする．");
        System.out.println("    --heatmap <TYPE> ヒートマップの種類を指定する．scoreもしくはtime．");
    }

    void drawHeatMap(Integer width, Integer height) {
        /*
            ヒートマップを描写する
        */
        EZ.initialize(width, height);
        Integer countY = 0;
        for (Integer question : this.questions) {
            Integer countX = 0;
            for (StudentScore5 score : this.scoreMap.values()) {
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

    void outputImage(Integer width, Integer height, String fileName) throws IOException {
        /*
            画像ファイルを作成する
        */
        if (fileName.equals("")) {
            fileName = "heatmap.png";
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        EZ.app.paintComponent(g); // image に図形を描画する．
        // image を "png" フォーマットで "heatmap.png" に出力する．
        ImageIO.write(image, "png", new File(fileName));
    }

    ArrayList<StudentScore5> sortedStudentList(HashMap<String, StudentScore5> map, String sortKey) {
        /*
            指定されてた方法でStudentScoreをソートする
        */
        ArrayList<StudentScore5> list = new ArrayList<>(map.values());

        if (Objects.equals(sortKey, "id")) {
            Collections.sort(list, new StudentIdComparator());
        } else if (Objects.equals(sortKey, "score")) {
            Collections.sort(list, new StudentScoreComparator());
        } else if (Objects.equals(sortKey, "time")) {
            Collections.sort(list, new StudentTakenTimeComparator());
        }
        return list;
    }

    void createStudentScore(File thisFile) throws IOException {
        /*
            ファイルを読み込みStudentScoreに値を入れる
        */
        BufferedReader in = new BufferedReader(new FileReader(thisFile));
        String line;
        while ((line = in.readLine()) != null) {
            String[] testInfo = line.split(",");
            if (!this.scoreMap.containsKey(testInfo[3])) {
                this.scoreMap.put(testInfo[3], new StudentScore5());
                this.scoreMap.get(testInfo[3]).id = Integer.valueOf(testInfo[3]);
            }
            putStudentScore(testInfo[3], testInfo[2], testInfo[4]);
            if (testInfo.length > 6) {
                putStudentTime(testInfo[3], testInfo[2], testInfo[5], testInfo[6]);
            }
        }
        in.close();
    }

    void putStudentTime(String id, String question, String beginTime, String finTime) {
        /*
        所用時間を追加する
        */

        if (!Objects.equals("", finTime)) {
            this.scoreMap.get(id).timeInfo.put(question, timeCounter(beginTime, finTime));
        }
    }

    Integer timeCounter(String beginTime, String finTime) {
        /*
            所要時間を計算する
        */

        String bTime[] = beginTime.split(":");
        String fTime[] = finTime.split(":");
        Integer bTimeInt = Integer.valueOf(bTime[0]) * 60 + Integer.valueOf(bTime[1]);
        Integer fTimeInt = Integer.valueOf(fTime[0]) * 60 + Integer.valueOf(fTime[1]);
        return fTimeInt - bTimeInt;
    }

    void putStudentScore(String id, String question, String value) {
        /*
            生徒のスコアを作成・追加する
            統計のデータを追加
            問題番号の種類を追加する
        */

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

    void printResult(ArrayList<StudentScore5> studentScoreList) {
        /*
            結果を出力する
        */

        for (StudentScore5 studentScore : studentScoreList) {
            System.out.printf("%s", studentScore.id);
            for (Integer question : this.questions) {
                printScore(studentScore, question);
                printTime(studentScore, question);
            }
            if (this.scoreMap.get(studentScore.id().toString()).stats.count() > 0) {
                System.out.printf(",%d", studentScore.stats.max());
                System.out.printf(",%d", studentScore.stats.min());
                System.out.printf(",%f", studentScore.stats.average());
            } else {//一問解いていないなら
                System.out.printf(",");
                System.out.printf(",");
                System.out.printf(",");
            }
            System.out.println();
        }
        printStats();
    }

    void printScore(StudentScore5 studentScore, Integer question) {
        /*
            各生徒の成績を出力
        */

        if (studentScore.scoreInfo.containsKey(question.toString())) {
            Integer number = studentScore.scoreInfo.get(question.toString());
            System.out.printf(",%d", number);
        } else {
            System.out.printf(",");
        }
    }

    void printTime(StudentScore5 studentScore, Integer question) {
        /*
            各生徒の所要時間を出力
        */
        if (studentScore.timeInfo.containsKey(question.toString())) {
            Integer number = studentScore.timeInfo.get(question.toString());
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
        ScoreAnalyzer5 application = new ScoreAnalyzer5();
        application.run(args);
    }
}