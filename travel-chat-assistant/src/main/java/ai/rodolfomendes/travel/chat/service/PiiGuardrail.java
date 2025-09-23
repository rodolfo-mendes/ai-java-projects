package ai.rodolfomendes.travel.chat.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

import java.util.regex.Pattern;

public class PiiGuardrail implements OutputGuardrail {

    // Regex to detect common email formats
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"
    );

    // Regex to detect common phone number formats (international and local)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "\\b(?:\\+?\\d{1,3}[-.\\s]?)?(?:\\(\\d{1,4}\\)|\\d{1,4})[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}\\b"
    );

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        var text = responseFromLLM.text();

        if (EMAIL_PATTERN.matcher(text).find()) {
            return failure("Output blocked: The response contains an email address.");
        }

        if (PHONE_PATTERN.matcher(text).find()) {
            return failure("Output blocked: The response contains a phone number.");
        }

        return successWith(text);
    }
}
