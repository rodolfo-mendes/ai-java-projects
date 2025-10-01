package ai.rodolfomendes.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @GetMapping("/chat")
    public String hello() {
        return "Hello World!";
    }
}
