package ai.rodolfomendes.travel.chat.service;

import com.mongodb.client.MongoClient;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfiguration {
    @Value("${travel.db.database}")
    private String database;

    @Value("${travel.db.chat-memory-collection}")
    private String chatMemory;

    @Bean
    public ChatMemoryProvider getChatMemoryProvider(MongoClient client) {
        return new MongoDbChatMemoryProvider(client, database, chatMemory);
    }
}
