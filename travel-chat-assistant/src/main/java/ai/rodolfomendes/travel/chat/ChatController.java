package ai.rodolfomendes.travel.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final TravelChatAssistant assistant;
    private final ChatHistory chatHistory;

    @Autowired
    public ChatController(TravelChatAssistant assistant, ChatHistory chatHistory) {
        this.assistant = assistant;
        this.chatHistory = chatHistory;
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return chatHistory.messages();
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMessage(@RequestBody NewUserMessage newUserMessage) {
        var assistantResponse = assistant.chat(newUserMessage.text());

        chatHistory.addUserMessage(newUserMessage.text());
        chatHistory.addAiMessage(assistantResponse);
    }
}
