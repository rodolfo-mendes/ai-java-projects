package ai.rodolfomendes.travel.chat;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfiguration {
    @Bean
    public ChatMemory getChatMemory() {
        return MessageWindowChatMemory.builder()
            .maxMessages(100)
            .chatMemoryStore(new InMemoryChatMemoryStore())
            .build();
    }
}
