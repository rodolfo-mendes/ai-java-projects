package ai.rodolfomendes.travel.chat.controller;

import ai.rodolfomendes.travel.chat.model.*;
import ai.rodolfomendes.travel.chat.service.TravelChatAssistant;
import dev.langchain4j.guardrail.GuardrailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final TravelChatAssistant assistant;
    private final Chat chat;

    @Autowired
    public ChatController(TravelChatAssistant assistant, Chat chat) {
        this.assistant = assistant;
        this.chat = chat;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void start() {
        startChatIfNotStarted();
    }

    private void startChatIfNotStarted() {
        if (!chat.isStarted()) {
            String id = chat.start().toString();
            logger.info("Chat started with id: {}", id);
            var greetingsMessage = assistant.greetings(Dates.currentDateFormatted(), chat.id().toString());
            chat.addAiMessage(greetingsMessage);
        }
    }

    @PostMapping("/messages")
    public void sendMessage(@RequestBody NewUserMessage newUserMessage) {
        startChatIfNotStarted();

        final var newUserMessageText = newUserMessage.text();
        final var aiMessageText = assistant.chat(
            newUserMessageText,
            Dates.currentDateFormatted(),
            chat.id().toString());

        chat.addUserMessage(newUserMessageText);
        chat.addAiMessage(aiMessageText);
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return chat.messages();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GuardrailException.class)
    public ChatError handleGuardrailException(GuardrailException e) {
        return new ChatError(e.getMessage());
    }
}
