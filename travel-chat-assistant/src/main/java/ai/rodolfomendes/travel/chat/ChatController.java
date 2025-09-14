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
}
