import java.util.ArrayList;

public class Arguments {
    ArrayList<String> args = new ArrayList<>();

    String dest = "";
    String sort = "";
    String heatmap = "";
    Boolean help = false;

    void parse(String[] args) {
        for (Integer i = 0; i < args.length; i++) {
            if (!args[i].startsWith("--")) {
                this.args.add(args[i]);
            } else {
                i = parseOption(args, i);
            }
        }
    }

    Integer parseOption(String[] args, Integer i) {
        switch (args[i]) {
            case "--dest":
                i++;
                this.dest = args[i];
                break;
            case "--sort":
                i++;
                this.sort = args[i];
                break;
            case "--heatmap":
                i++;
                this.heatmap = args[i];
                break;
            case "--help":
                i++;
                this.help = true;
                break;
        }
        return i;
    }
}