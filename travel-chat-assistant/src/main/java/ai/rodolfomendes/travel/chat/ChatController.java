package ai.rodolfomendes.travel.chat;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private ChatModel chatModel;

    @Autowired
    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping
    @RequestMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @PostMapping
    public String respondMessage(@RequestBody String message) {
        var template = """
            Your message:
            {{userMessage}}
            
            Response:
            {{response}}
            """;

        var response = chatModel.chat(message);

        return template
            .replace("{{userMessage}}", message)
            .replace("{{response}}", response);
    }
}
