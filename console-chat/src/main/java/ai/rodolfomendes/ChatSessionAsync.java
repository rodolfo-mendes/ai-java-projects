package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;

public class ChatSessionAsync implements ChatSession{
    private final StreamingChatModel chatModel;
    private final ChatMemory memory;
    private final Object monitor = new Object();

    public ChatSessionAsync(StreamingChatModel chatModel, ChatMemory memory) {
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

            
                System.out.println("Type your question: ");

                String userInput = br.readLine();
                ChatMessage userMessage = UserMessage.from(userInput);
                memory.add(userMessage);

                System.out.println();
                System.out.println("AI:");

                chatModel.chat(memory.messages(), new StreamingChatResponseHandler() {
                    @Override
                    public void onPartialResponse(String partialResponse) {
                        System.out.print(partialResponse);
                    }

                    @Override
                    public void onCompleteResponse(ChatResponse completeResponse) {
                        synchronized (monitor) {
                            System.out.println();
                            memory.add(completeResponse.aiMessage());
                            monitor.notifyAll();
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        throw new RuntimeException(error);
                    }
                });

                synchronized (monitor) {
                    monitor.wait();
                }
            System.out.println("*** END ***");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
