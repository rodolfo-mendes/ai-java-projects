package ai.rodolfomendes.travel.chat;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

import java.util.List;

public class OffensiveLanguageGuardrail implements InputGuardrail {
    /**
     * Note: This list is intended for educational and moderation purposes only.
     * It avoids explicit slurs and focuses on terms commonly used in harmful contexts.
     */
    private static final List<String> OFFENSIVE_KEYWORDS = List.of(
            "damn",
            "hell",
            "crap",
            "bastard",
            "nazi",
            "terrorist",
            "supremacist",
            "bigot",
            "stalker",
            "creep",
            "freak",
            "psycho",
            "bimbo",
            "slut",
            "whore",
            "emasculate",
            "monkey",
            "savage",
            "ape",
            "foreigner"
    );

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        var words = userMessage.singleText().toLowerCase().split("\\s");

        for (var word : words) {
            if (OFFENSIVE_KEYWORDS.contains(word)) {
                return failure("User input contains offensive language: " + word);
            }
        }

        return success();
    }
}
