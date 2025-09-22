package ai.rodolfomendes.travel;

import ai.rodolfomendes.travel.chat.PiiGuardrail;
import dev.langchain4j.data.message.AiMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PiiGuardrailTest {
    @Test
    public void emailInMessageShouldFail() {
        var piiGuardrail = new PiiGuardrail();

        var aiMessage = AiMessage.builder()
            .text("Jennifer Miller's email is jennifer.miller@pinnacletravel.com.")
            .build();

        var result = piiGuardrail.validate(aiMessage);

        assertNotNull(result);
        assertFalse(result.isSuccess());
    }
}
