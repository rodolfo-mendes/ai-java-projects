package ai.rodolfomendes.travel.chat;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
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
            Hello! This is your Travel Chat Assistant! How can I help you?
            """;

        return template.replace("{{userMessage}}", message);
    }
}
