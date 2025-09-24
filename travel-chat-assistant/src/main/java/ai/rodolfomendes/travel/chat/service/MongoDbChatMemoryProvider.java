package ai.rodolfomendes.travel.chat.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class MongoDbChatMemoryProvider implements ChatMemoryProvider {
    private final Map<String, ChatMemory> memoryMap;
    private final MongoDatabase database;

    public MongoDbChatMemoryProvider(MongoDatabase database) {
        this.database = database;
        this.memoryMap = new HashMap<>();
    }

    @Override
    public ChatMemory get(Object memoryId) {
        if (memoryId == null) {
            throw new NullPointerException(
                "MongoDbChatMemoryProvider.get: memoryId cannot be null.");
        }

        if (!(memoryId instanceof String)) {
            throw new IllegalArgumentException(
                "MongoDbChatMemoryProvider.get: memoryId must be of type " + String.class.getName());
        }

        return getOrCreate((String) memoryId);
    }

    private ChatMemory getOrCreate(String memoryId) {
        final var existingMemory = memoryMap.get(memoryId);
        if (existingMemory != null) {
            return existingMemory;
        }

        synchronized (memoryMap) {
            if (!memoryMap.containsKey(memoryId)) {
                final var newMemory = MessageWindowChatMemory.builder()
                    .id(memoryId)
                    .maxMessages(100)
                    .chatMemoryStore(new MongoDbChatMemoryStore(this.database))
                    .build();

                memoryMap.put(memoryId, newMemory);

                return newMemory;
            }
        }

        return memoryMap.get(memoryId);
    }
}
