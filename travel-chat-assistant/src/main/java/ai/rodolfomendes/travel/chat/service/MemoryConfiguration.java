package ai.rodolfomendes.travel.chat.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfiguration {
    @Bean
    public ChatMemoryProvider getChatMemoryProvider() {
        return new InMemoryChatMemoryProvider();
    }
}
