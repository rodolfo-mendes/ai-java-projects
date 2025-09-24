package ai.rodolfomendes.travel.chat.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;
import static org.springframework.data.mongodb.core.BulkOperationsExtensionsKt.upsert;

public class MongoDbChatMemoryStore implements ChatMemoryStore {
    private final Logger logger = LoggerFactory.getLogger(MongoDbChatMemoryStore.class);
    private final MongoDatabase database;

    public MongoDbChatMemoryStore(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        logger.info("Retrieving messages for memory {}", memoryId);

        MongoCollection<Document> collection = database.getCollection("chat");
        Document memoryDocument = collection
            .find(eq("memoryId", memoryId))
            .projection(include("messages"))
            .first();

        if (memoryDocument == null) {
            return Collections.emptyList();
        }

        List<Document> messages = (List<Document>) memoryDocument.get("messages");

        return documentListToMessages(messages);
    }

    private List<ChatMessage> documentListToMessages(List<Document> messages) {
        return messages.stream()
            .map(this::documentToMessage)
            .toList();
    }

    private ChatMessage documentToMessage(Document document) {
        var type = ChatMessageType.valueOf((String) document.get("type"));
        var text = (String) document.get("text");

        return switch (type) {
            case SYSTEM -> SystemMessage.systemMessage(text);
            case USER -> UserMessage.userMessage(text);
            case AI -> AiMessage.aiMessage(text);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        logger.info("Updating messages for memory {}", memoryId);

        MongoCollection<Document> collection = database.getCollection("chat");
        collection.updateOne(
            eq("memoryId", memoryId),
            set("messages", messagesToDocumentList(messages)),
            new UpdateOptions().upsert(true));
    }

    private List<Document> messagesToDocumentList(List<ChatMessage> messages) {
        return messages.stream()
            .map(this::messageToDocument)
            .toList();
    }

    private Document messageToDocument(ChatMessage message) {
        var messageDocument = new Document();
        messageDocument.put("type", message.type().name());
        messageDocument.put("text", getMessageText(message));
        return messageDocument;
    }

    private String getMessageText(ChatMessage message) {
        return switch (message.type()) {
            case SYSTEM -> ((SystemMessage) message).text();
            case USER -> ((UserMessage) message).singleText();
            case AI -> ((AiMessage) message).text();
            default -> "-- no message --";
        };
    }

    @Override
    public void deleteMessages(Object memoryId) {
        MongoCollection<Document> collection = database.getCollection("chat");
        collection.deleteOne(eq("memoryId", memoryId));
    }
}
