package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;

public class ChatSessionSync implements ChatSession{
    private final ChatModel chatModel;
    private final ChatMemory memory;

    public ChatSessionSync(ChatModel chatModel, ChatMemory memory) {
        this.chatModel = chatModel;
        this.memory = memory;
    }

    // The chat logic depends only on the ChatModel interface
    @Override
    public void chat() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("*** Starting chat ***");
            System.out.printf("Provider: %s%n", chatModel.provider().name());
            System.out.printf("Model: %s%n%n", chatModel.defaultRequestParameters().modelName());

            while (true) {
                System.out.println("Type your question: ");

                String userInput = br.readLine();
                if (userInput.equalsIgnoreCase("/BYE")) {
                    break;
                }

                ChatMessage userMessage = UserMessage.from(userInput);
                memory.add(userMessage);

                ChatResponse response = chatModel.chat(memory.messages());

                AiMessage aiMessage = response.aiMessage();
                memory.add(aiMessage);
                
                System.out.println();
                System.out.println("AI:");
                System.out.println(aiMessage.text());
            }

            System.out.println("*** END ***");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
