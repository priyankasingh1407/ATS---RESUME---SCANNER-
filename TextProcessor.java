
import java.util.*;

public class TextProcessor {

    private static final Set<String> STOP_WORDS = new HashSet<>(
        Arrays.asList(
            "the", "is", "are", "and", "or", "a", "an",
            "to", "of", "in", "for", "with", "on", "at",
            "by", "this", "that", "will", "be", "have",
            "has", "as", "we", "you", "your", "our",
            "from", "it", "their", "they", "can", "should",
            "i", "me", "my", "he", "she", "was", "were",
            "which", "who", "what", "when", "where"
        )
    );

    public List<String> tokenize(String text) {
        List<String> words = new ArrayList<>();

        String cleaned = text.toLowerCase()
            .replaceAll("[^a-z0-9+#.]", " ");

        for (String word : cleaned.split("\\s+")) {
            if (word.length() > 1 &&
                !STOP_WORDS.contains(word)) {
                words.add(word);
            }
        }

        return words;
    }
}
