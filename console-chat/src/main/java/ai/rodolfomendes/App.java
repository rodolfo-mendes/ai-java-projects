package ai.rodolfomendes;

import static ai.rodolfomendes.ModelBuilder.buildChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

/**
 * Hello world!
 */
public class App {

  
    public static void main(String[] args) {
        System.out.println("program started ...");

        final ChatModel chatModel = buildChatModel(parseModelName(args));

        //StreamingChatModel streamingChatModel = buildStreamingChatModel(parseModelName(args));
       
        final ChatMemory memory = MessageWindowChatMemory.builder()
                .id("simple-chat-memory")
                .maxMessages(100)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();

        final SimpleChat simpleChat = AiServices.builder(SimpleChat.class)
            .chatModel(chatModel)
            .chatMemory(memory)
            .build();

        final ChatSession chatSession = new ChatSessionSimpleChat(simpleChat);
        chatSession.chat();       
    }

    private static String parseModelName(String[] args) {
        if (args == null || args.length == 0) {
            return "ollama";
        }

        return args[0];
    }
}
