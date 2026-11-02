
import java.util.*;

public class Scorer {

    public ScoreResult score(
        Map<String, Integer> resumeKw,
        Map<String, Integer> jdKw
    ) {
        Set<String> matched = new TreeSet<>(jdKw.keySet());
        matched.retainAll(resumeKw.keySet());

        Set<String> missing = new TreeSet<>(jdKw.keySet());
        missing.removeAll(resumeKw.keySet());

        double percentage = 0;

        if (!jdKw.isEmpty()) {
            percentage =
                (matched.size() * 100.0) / jdKw.size();
        }

        return new ScoreResult(
            "Resume",
            percentage,
            new ArrayList<>(matched),
            new ArrayList<>(missing)
        );
    }
}
