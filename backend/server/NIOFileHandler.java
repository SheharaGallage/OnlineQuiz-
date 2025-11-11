package server;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.LocalDateTime;

public class NIOFileHandler {
    private static final String QUESTIONS_FILE = "backend/data/questions.json";
    private static final String RESULTS_FILE = "backend/data/results.txt";

    // Load quiz questions using NIO
    public String loadQuizQuestions() throws IOException {
        Path path = Paths.get(QUESTIONS_FILE);

        if (!Files.exists(path)) {
            createDefaultQuestions();
        }

        return Files.readString(path, StandardCharsets.UTF_8);
    }

    // Save quiz results using NIO
    public void saveQuizResult(String userId, int score, int total) throws IOException {
        Path path = Paths.get(RESULTS_FILE);

        String result = String.format("%s | User: %s | Score: %d/%d%n",
                LocalDateTime.now(),
                userId,
                score,
                total);

        Files.writeString(path, result,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private void createDefaultQuestions() throws IOException {
        String defaultQuiz = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"question\": \"What does TCP stand for?\",\n" +
                "    \"options\": [\"Transfer Control Protocol\", \"Transmission Control Protocol\", \"Transport Control Protocol\", \"Transcription Control Protocol\"],\n"
                +
                "    \"correctAnswer\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"question\": \"Which port does HTTP use by default?\",\n" +
                "    \"options\": [\"21\", \"22\", \"80\", \"443\"],\n" +
                "    \"correctAnswer\": 2\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"question\": \"What is the main advantage of UDP over TCP?\",\n" +
                "    \"options\": [\"Reliability\", \"Speed\", \"Security\", \"Error checking\"],\n" +
                "    \"correctAnswer\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 4,\n" +
                "    \"question\": \"Which Java class is used for socket programming?\",\n" +
                "    \"options\": [\"Socket\", \"Connection\", \"Network\", \"Link\"],\n" +
                "    \"correctAnswer\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 5,\n" +
                "    \"question\": \"What does NIO stand for in Java?\",\n" +
                "    \"options\": [\"Network Input Output\", \"New Input Output\", \"Node Input Output\", \"Next Input Output\"],\n"
                +
                "    \"correctAnswer\": 1\n" +
                "  }\n" +
                "]";

        Path path = Paths.get(QUESTIONS_FILE);
        Files.createDirectories(path.getParent());
        Files.writeString(path, defaultQuiz, StandardCharsets.UTF_8);
    }
}