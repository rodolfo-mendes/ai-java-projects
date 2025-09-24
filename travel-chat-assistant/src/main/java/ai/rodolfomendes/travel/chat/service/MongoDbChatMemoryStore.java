package ai.rodolfomendes.travel.chat.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.bson.Document;

import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

public class MongoDbChatMemoryStore implements ChatMemoryStore {
    private final MongoClient mongoClient;
    private final String database;
    private final String chatMemory;

    public MongoDbChatMemoryStore(MongoClient mongoClient, String database, String chatMemory) {
        this.mongoClient = mongoClient;
        this.database = database;
        this.chatMemory = chatMemory;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        MongoDatabase database = mongoClient.getDatabase(this.database);
        MongoCollection<Document> collection = database.getCollection(this.chatMemory);
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
        MongoDatabase database = mongoClient.getDatabase(this.database);
        MongoCollection<Document> collection = database.getCollection(this.chatMemory);
        collection.updateOne(
            eq("memoryId", memoryId),
            set("messages", messagesToDocumentList(messages)));
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
            default -> "";
        };
    }

    @Override
    public void deleteMessages(Object memoryId) {
        MongoDatabase database = mongoClient.getDatabase(this.database);
        MongoCollection<Document> collection = database.getCollection(this.chatMemory);
        collection.deleteOne(eq("memoryId", memoryId));
    }
}
