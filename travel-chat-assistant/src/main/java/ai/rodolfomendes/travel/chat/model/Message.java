package ai.rodolfomendes.travel.chat.model;

public record Message(
    Integer index,
    MessageType type,
    String text
) {}
