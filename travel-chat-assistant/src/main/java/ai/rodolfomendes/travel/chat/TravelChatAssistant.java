package ai.rodolfomendes.travel.chat;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.spring.AiService;

@AiService
@InputGuardrails({OffensiveLanguageGuardrail.class})
public interface TravelChatAssistant {
    @SystemMessage(fromResource = "assistant-system-message.txt")
    String chat(
        @UserMessage String userMessage,
        @V("current-date-formatted") String currentDataFormatted);

    @SystemMessage(fromResource = "assistant-system-message.txt")
    @UserMessage("""
        Based on your role, respond the user with a greetings message. Briefly guide the user
        trough the process of planning a trip. Clarify the user how can you help them in each
        step of the process.
    """)
    String greetings(@V("current-date-formatted") String currentDataFormatted);
}
