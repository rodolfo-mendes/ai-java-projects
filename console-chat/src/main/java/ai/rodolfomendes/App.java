package ai.rodolfomendes;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
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

        //final ChatModel chatModel = buildChatModel(parseModelName(args));

        StreamingChatModel streamingChatModel = buildStreamingChatModel(parseModelName(args));
       
        final ChatMemory memory = MessageWindowChatMemory.builder()
                .id("simple-chat-memory")
                .maxMessages(100)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();

        final ChatSession chatSession = new ChatSessionAsync(streamingChatModel, memory);
        chatSession.chat();       
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
    
    private static StreamingChatModel buildStreamingChatModel(String provider) {
        if ("openai".equalsIgnoreCase(provider)) {
            String openAiApiKey = System.getenv("OPENAI_KEY");

            if (openAiApiKey == null || openAiApiKey.isBlank()) {
                throw new IllegalArgumentException("Open AI API Key not found.");
            }

            return OpenAiStreamingChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(OPENAI_MODEL_NAME)
                .temperature(OPENAI_TEMPERATURE)
                .maxCompletionTokens(OPENAI_MAX_COMPLETION_TOKENS)
                .build();
        }

        // local Ollama is our default provider
        return OllamaStreamingChatModel.builder()
                .baseUrl(OLLAMA_BASE_URL)
                .modelName(OLLAMA_MODEL_NAME)
                .think(false)
                .build();
    }
}
