package ai.rodolfomendes.travel.chat.model;

import java.util.UUID;

public class ChatId {
    private final String stringChatId;

    private ChatId(String stringChatId) {
        this.stringChatId = stringChatId;
    }

    public static ChatId build() {
        return new ChatId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return this.stringChatId;
    }

    @Override
    public int hashCode() {
        return this.stringChatId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ChatId)) {
            return false;
        }

        return this.stringChatId.equals(((ChatId) obj).stringChatId);
    }
}
