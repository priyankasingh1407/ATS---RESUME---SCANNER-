
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ResumeParser {

    public String extractText(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("Resume file not found!");
        }

        String name = file.getName().toLowerCase();

        if (!name.endsWith(".txt")) {
            throw new IOException("Only TXT files are supported currently.");
        }

        String text = Files.readString(
            file.toPath(), StandardCharsets.UTF_8
        );

        if (text.isBlank()) {
            throw new IOException("Resume file is empty!");
        }

        return text;
    }
}
