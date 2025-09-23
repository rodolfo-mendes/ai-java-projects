package ai.rodolfomendes.travel.chat.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ai.rodolfomendes.travel.chat.model.MessageType.*;

public class ChatHistory {
    private final List<Message> messages = new ArrayList<>();
    private int nextIndex = 0;

    public void addUserMessage(String text) {
        messages.add(new Message(nextIndex++, USER, text));
    }

    public void addAiMessage(String text) {
        messages.add(new Message(nextIndex++, AI, text));
    }

    public List<Message> messages() {
        return Collections.unmodifiableList(messages);
    }
}
