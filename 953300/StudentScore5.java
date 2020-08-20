import java.util.HashMap;

public class StudentScore5 {
    /*
           1生徒のスコア
    */

    HashMap<String, Integer> scoreInfo = new HashMap<>();
    HashMap<String, Integer> timeInfo = new HashMap<>();
    Stats stats = new Stats();

    Integer id;

    Integer max() {
        return this.stats.max();
    }

    Integer min() {
        return this.stats.min();
    }

    Double average() {
        return this.stats.average();
    }

    Double averageOfTakenTime() {
        return (double) this.timeInfo.values().stream().mapToInt(x -> x).sum() / this.timeInfo.size();
    }

    Integer id() {
        return this.id;
    }
}