package ai.rodolfomendes.travel.chat;

import dev.langchain4j.service.spring.AiService;

@AiService
public interface TravelChatAssistant {
    String chat(String userMessage);
}
