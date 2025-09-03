package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

        ChatModel chatModel = buildChatModel();

        doChat(chatModel);

        System.out.println("*** END ***");
    }

    private static ChatModel buildChatModel() {
        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .think(false)
                .build();
                
        return chatModel;
    }

    // The chat logic depends only on the ChatModel interface
    private static void doChat(ChatModel chatModel) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Type your question: ");
                String question = br.readLine();
                String aiMessage = chatModel.chat(question);
                System.out.println();
                System.out.println("AI:");
                System.out.println(aiMessage);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
