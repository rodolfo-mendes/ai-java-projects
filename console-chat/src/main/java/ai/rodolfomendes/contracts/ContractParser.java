package ai.rodolfomendes.contracts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ai.rodolfomendes.ModelBuilder;
import dev.langchain4j.data.message.SystemMessage;

public class ContractParser {
    public static void main(String[] args) {
        var model = ModelBuilder.buildChatModel("openai");

        var systemMessage = SystemMessage.from(readSystemPrompt());
        var userMessage = SystemMessage.from(readUserPrompt());

        var response = model.chat(systemMessage, userMessage);

        System.out.println(response.aiMessage().text());       
    }

    private static String readSystemPrompt() {
        InputStream is = ContractParser.class.getResourceAsStream("prompts/contract-parser.txt");
        String prompt = readStringFromStream(is);
        return prompt;
    }

    private static String readUserPrompt() {
        InputStream is = ContractParser.class.getResourceAsStream("prompts/user-prompt.txt");
        String template = readStringFromStream(is);

        InputStream is2 = ContractParser.class.getResourceAsStream("prompts/contract.txt");
        String contract = readStringFromStream(is2);

        return template.replace("{{contract}}", contract);
    }

    private static String readStringFromStream(InputStream is) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while loading file", e);
        } 
    }
}
