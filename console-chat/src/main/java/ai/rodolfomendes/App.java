package ai.rodolfomendes;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

/**
 * Hello world!
 */
public class App {
    final static String MODEL_NAME = "gemma3:1b";
    final static String BASE_URL = "http://localhost:11434";

    public static void main(String[] args) {
        System.out.printf("*** Starting chat with model: %s ***%n", MODEL_NAME);

        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .think(false)
                .build();

        String userMessage = "Say hello";
        System.out.printf("User: %s%n", userMessage);
        String aiMessage = chatModel.chat("Say hello");
        System.out.printf("AI: %s%n", aiMessage);

        System.out.println("*** END ***");
    }
}
