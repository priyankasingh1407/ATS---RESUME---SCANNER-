
import java.util.*;

public class KeywordExtractor {

    private final TextProcessor processor = new TextProcessor();

    public Map<String, Integer> extract(String text) {
        Map<String, Integer> frequency = new HashMap<>();

        List<String> words = processor.tokenize(text);

        for (String word : words) {
            frequency.put(
                word,
                frequency.getOrDefault(word, 0) + 1
            );
        }

        return frequency;
    }
}
