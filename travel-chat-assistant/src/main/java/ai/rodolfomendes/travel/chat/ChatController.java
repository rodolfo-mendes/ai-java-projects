package ai.rodolfomendes.travel.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final TravelChatAssistant assistant;

    @Autowired
    public ChatController(TravelChatAssistant assistant) {
        this.assistant = assistant;
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

        var response = assistant.chat(message);

        return template
            .replace("{{userMessage}}", message)
            .replace("{{response}}", response);
    }
}
