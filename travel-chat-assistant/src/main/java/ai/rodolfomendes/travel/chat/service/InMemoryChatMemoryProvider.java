package ai.rodolfomendes.travel.chat.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

import java.util.HashMap;
import java.util.Map;

public class InMemoryChatMemoryProvider implements ChatMemoryProvider {
    private final Map<String, ChatMemory> memoryMap;

    public InMemoryChatMemoryProvider() {
        this.memoryMap = new HashMap<>();
    }

    @Override
    public ChatMemory get(Object memoryId) {
        if (memoryId == null) {
            throw new NullPointerException(
                "InMemoryChatMemoryProvider.get: memoryId cannot be null.");
        }

        if (!(memoryId instanceof String)) {
            throw new IllegalArgumentException(
                "InMemoryChatMemoryProvider.get: memoryId must be of type " + String.class.getName());
        }

        return getOrCreate((String) memoryId);
    }

    private ChatMemory getOrCreate(String memoryId) {
        ChatMemory memory = memoryMap.get(memoryId);
        if (memory != null) {
            return memory;
        }

        synchronized (memoryMap) {
            if (!memoryMap.containsKey(memoryId)) {
                memory = MessageWindowChatMemory.builder()
                    .id(memoryId)
                    .maxMessages(100)
                    .chatMemoryStore(new InMemoryChatMemoryStore())
                    .build();

                memoryMap.put(memoryId, memory);
            }
        }

        return memory;
    }
}
