package ai.rodolfomendes.travel.chat.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Component
@SessionScope
public class Chat {
    private final ChatId id;
    private final ChatHistory history;
    private final ChatState state;

    public Chat() {
        this.id = ChatId.build();
        this.history = new ChatHistory();
        this.state = new ChatState();
    }

    public ChatId id() {
        return id;
    }

    public ChatId start() {
        state.start();
        return id;
    }

    public boolean isStarted() {
        return state.isStarted();
    }

    public void finish() {
        state.finish();
    }

    public boolean isFinished() {
        return state.isFinished();
    }

    public void addUserMessage(String text) {
        history.addUserMessage(text);
    }

    public void addAiMessage(String text) {
        history.addAiMessage(text);
    }

    public List<Message> messages() {
        return history.messages();
    }
}
