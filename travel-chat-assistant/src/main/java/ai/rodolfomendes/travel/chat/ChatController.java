package ai.rodolfomendes.travel.chat;

import dev.langchain4j.guardrail.GuardrailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final TravelChatAssistant assistant;
    private final ChatHistory chatHistory;
    private final ChatState state;

    @Autowired
    public ChatController(TravelChatAssistant assistant, ChatHistory chatHistory) {
        this.assistant = assistant;
        this.chatHistory = chatHistory;
        this.state = new ChatState();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void start() {
        startChatIfNotStarted();
    }

    private void startChatIfNotStarted() {
        if (!state.isStarted()) {
            var greetingsMessage = assistant.greetings(Dates.currentDateFormatted());
            chatHistory.addAiMessage(greetingsMessage);
            state.start();
        }
    }

    @PostMapping("/messages")
    public void sendMessage(@RequestBody NewUserMessage newUserMessage) {
        startChatIfNotStarted();

        final var newUserMessageText = newUserMessage.text();
        final var aiMessageText = assistant.chat(newUserMessageText, Dates.currentDateFormatted());

        chatHistory.addUserMessage(newUserMessageText);
        chatHistory.addAiMessage(aiMessageText);
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return chatHistory.messages();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GuardrailException.class)
    public ChatError handleGuardrailException(GuardrailException e) {
        return new ChatError(e.getMessage());
    }
}
