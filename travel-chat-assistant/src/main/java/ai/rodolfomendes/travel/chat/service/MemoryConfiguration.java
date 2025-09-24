package ai.rodolfomendes.travel.chat.service;

import com.mongodb.client.MongoClient;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfiguration {
    @Bean
    public ChatMemoryProvider getChatMemoryProvider(MongoClient client) {
        return new MongoDbChatMemoryProvider(client.getDatabase("travel"));
        //return new InMemoryChatMemoryProvider();
    }
}
