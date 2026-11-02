
import java.io.File;
import java.io.IOException;
import java.util.*;

public class RankingService {

    private final ResumeParser parser = new ResumeParser();
    private final KeywordExtractor extractor = new KeywordExtractor();
    private final Scorer scorer = new Scorer();

    public List<ScoreResult> rankResumes(
        List<File> files,
        String jobDescription
    ) throws IOException {

        Map<String, Integer> jdKeywords =
            extractor.extract(jobDescription);

        PriorityQueue<ScoreResult> queue =
            new PriorityQueue<>(
                Comparator.comparingDouble(ScoreResult::getScore)
                    .reversed()
            );

        for (File file : files) {
            String text = parser.extractText(file);

            Map<String, Integer> resumeKeywords =
                extractor.extract(text);

            ScoreResult result =
                scorer.score(resumeKeywords, jdKeywords);

            ScoreResult namedResult = new ScoreResult(
                file.getName(),
                result.getScore(),
                result.getMatched(),
                result.getMissing()
            );

            queue.offer(namedResult);
        }

        List<ScoreResult> ranking = new ArrayList<>();

        while (!queue.isEmpty()) {
            ranking.add(queue.poll());
        }

        return ranking;
    }
}
