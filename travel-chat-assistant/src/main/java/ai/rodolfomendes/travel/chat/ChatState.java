package ai.rodolfomendes.travel.chat;

public class ChatState {
    public enum State {
        NOT_STARTED,
        STARTED,
        FINISHED
    }

    private State state = State.NOT_STARTED;

    public void start() {
        if (isFinished()) {
            throw new IllegalStateException("Chat state is already FINISHED!");
        }

        state = State.STARTED;
    }

    public boolean isStarted() {
        return state == State.STARTED;
    }

    public void finish() {
        state = State.FINISHED;
    }

    public boolean isFinished() {
        return state == State.FINISHED;
    }
}
