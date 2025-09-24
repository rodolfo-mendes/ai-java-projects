package ai.rodolfomendes.travel.chat.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import dev.langchain4j.service.spring.AiService;

@AiService
@InputGuardrails({OffensiveLanguageGuardrail.class})
public interface TravelChatAssistant {
    @SystemMessage(fromResource = "assistant-system-message.txt")
    @OutputGuardrails({PiiGuardrail.class})
    String chat(
        @UserMessage String userMessage,
        @V("current-date-formatted") String currentDataFormatted,
        @MemoryId String chatId);

    @SystemMessage(fromResource = "assistant-system-message.txt")
    @UserMessage(fromResource = "greetings.txt")
    String greetings(
        @V("current-date-formatted") String currentDataFormatted,
        @MemoryId String chatId);
}
