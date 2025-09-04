package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * Hello world!
 */
public class App {
    final static String MODEL_NAME = "gemma3:1b";
    final static String BASE_URL = "http://localhost:11434";
    final static Double GPT_TEMPERATURE = 1.0;
    final static Integer GPT_MAX_COMPLETION_TOKENS = 10_000;


    public static void main(String[] args) {
        System.out.println("program started ...");

        ChatModel chatModel = buildChatModel(parseModelName(args));

        System.out.println("*** Starting chat ***");
        System.out.printf("Provider: %s%n", chatModel.provider().name());
        System.out.printf("Model: %s%n", chatModel.defaultRequestParameters().modelName());

        doChat(chatModel);

        System.out.println("*** END ***");
    }

    private static String parseModelName(String[] args) {
        if (args == null || args.length == 0) {
            return "ollama";
        }

        return args[0];
    }

    private static ChatModel buildChatModel(String provider) {
        if ("openai".equalsIgnoreCase(provider)) {
            String openAiApiKey = System.getenv("OPENAI_KEY");

            if (openAiApiKey == null || openAiApiKey.isBlank()) {
                throw new IllegalArgumentException("Open AI API Key not found.");
            }

            return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName("gpt-4.1-nano")
                .temperature(GPT_TEMPERATURE)
                .maxCompletionTokens(GPT_MAX_COMPLETION_TOKENS)
                .build();
        }

        // local Ollama is our default provider
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .think(false)
                .build();
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
