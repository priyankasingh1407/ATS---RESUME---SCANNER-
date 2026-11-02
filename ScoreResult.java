
import java.util.*;

public class ScoreResult {

    private String fileName;
    private double score;
    private List<String> matched;
    private List<String> missing;

    public ScoreResult(
        String fileName,
        double score,
        List<String> matched,
        List<String> missing
    ) {
        this.fileName = fileName;
        this.score = score;
        this.matched = new ArrayList<>(matched);
        this.missing = new ArrayList<>(missing);
    }

    public String getFileName() {
        return fileName;
    }

    public double getScore() {
        return score;
    }

    public List<String> getMatched() {
        return matched;
    }

    public List<String> getMissing() {
        return missing;
    }
}
