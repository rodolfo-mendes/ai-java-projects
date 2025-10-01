package ai.rodolfomendes.chat;

import dev.langchain4j.service.spring.AiService;

@AiService
public interface ChatService {
    String chat(String userMessage);
}
