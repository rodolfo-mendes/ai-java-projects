package ai.rodolfomendes.travel.chat;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ai.rodolfomendes.travel.chat.MessageType.*;

@Component
public class ChatHistory {
    private final List<Message> messages = new ArrayList<>();
    private int nextIndex = 0;

    public void addUserMessage(String text) {
        messages.add(new Message(nextIndex++, USER, text));
    }

    public void addSystemMessage(String text) {
        messages.add(new Message(nextIndex++, SYSTEM, text));
    }

    public void addAiMessage(String text) {
        messages.add(new Message(nextIndex++, AI, text));
    }

    public List<Message> messages() {
        return Collections.unmodifiableList(messages);
    }
}
