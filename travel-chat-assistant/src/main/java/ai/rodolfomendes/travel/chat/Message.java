package ai.rodolfomendes.travel.chat;

public record Message(
    Integer index,
    MessageType type,
    String text
) {}
