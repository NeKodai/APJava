public class Stats {
    /*
        統計
    */

    Integer max = 0;
    Integer min = Integer.MAX_VALUE;
    Integer count = 0;
    Integer sum = 0;

    void put(Integer num) {
        this.max = num > this.max ? num : this.max;
        this.min = num < this.min ? num : this.min;
        this.sum += num;
        this.count += 1;
    }

    Integer max() {
        return this.max;
    }

    Integer min() {
        return this.min;
    }

    Integer count() {
        return this.count;
    }

    Double average() {
        return (double) this.sum / this.count;
    }
}