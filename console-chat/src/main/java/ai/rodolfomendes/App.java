package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

/**
 * Hello world!
 */
public class App {
    final static String OLLAMA_MODEL_NAME = "gemma3:1b";
    final static String OLLAMA_BASE_URL = "http://localhost:11434";
    final static Double OPENAI_TEMPERATURE = 1.0;
    final static Integer OPENAI_MAX_COMPLETION_TOKENS = 10_000;
    final static String OPENAI_MODEL_NAME = "gpt-4.1-nano";


    public static void main(String[] args) {
        System.out.println("program started ...");

        ChatModel chatModel = buildChatModel(parseModelName(args));
       
        ChatMemory memory = MessageWindowChatMemory.builder()
                .id("simple-chat-memory")
                .maxMessages(100)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();

        new App().chat(chatModel, memory);        
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
                .modelName(OPENAI_MODEL_NAME)
                .temperature(OPENAI_TEMPERATURE)
                .maxCompletionTokens(OPENAI_MAX_COMPLETION_TOKENS)
                .build();
        }

        // local Ollama is our default provider
        return OllamaChatModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(OLLAMA_MODEL_NAME)
                .think(false)
                .build();
    }

    // The chat logic depends only on the ChatModel interface
    private void chat(ChatModel chatModel, ChatMemory memory) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("*** Starting chat ***");
            System.out.printf("Provider: %s%n", chatModel.provider().name());
            System.out.printf("Model: %s%n%n", chatModel.defaultRequestParameters().modelName());

            while (true) {
                System.out.println("Type your question: ");

                String userInput = br.readLine();
                if (userInput.equalsIgnoreCase("/BYE")) {
                    break;
                }

                ChatMessage userMessage = UserMessage.from();
                memory.add(userMessage);

                ChatResponse response = chatModel.chat(memory.messages());

                AiMessage aiMessage = response.aiMessage();
                memory.add(aiMessage);
                
                System.out.println();
                System.out.println("AI:");
                System.out.println(aiMessage.text());
            }

            System.out.println("*** END ***");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
