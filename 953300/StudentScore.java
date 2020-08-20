import java.util.HashMap;

public class StudentScore {
    /*
           1生徒のスコア
    */

    HashMap<String, Integer> scoreInfo = new HashMap<>();
    Stats stats = new Stats();

    Integer max() {
        return stats.max();
    }

    Integer min() {
        return stats.min();
    }

    Double average() {
        return stats.average();
    }
}