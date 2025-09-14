package ai.rodolfomendes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import ai.rodolfomendes.contracts.Address;
import ai.rodolfomendes.contracts.Contract;
import ai.rodolfomendes.contracts.Person;
import ai.rodolfomendes.contracts.Schemas;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonSchema;

public class ContractParser {
    public static void main(String[] args) throws Exception{
        var model = ModelBuilder.buildChatModel("openai");

        var request = ChatRequest.builder()
            .responseFormat(jsonContractFormat())
            .messages(
                SystemMessage.from(readSystemPrompt()),
                UserMessage.from(readUserPrompt())
            )
            .build();

        var contractJsonString = model.chat(request)
            .aiMessage()
            .text();

        var contract = new ObjectMapper().readValue(contractJsonString, Contract.class);

        System.out.println(formatContractCustom(contract));
    }

    private static ResponseFormat jsonContractFormat() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .name("Contract")
                        .rootElement(Schemas.contractSchema())
                        .build())
                .build();
    }

    private static String readSystemPrompt() {
        return readStringFromStream(ContractParser.class
            .getResourceAsStream("contracts/prompts/contract-parser.txt"));
    }

    private static String readUserPrompt() {
        final var template = readStringFromStream(ContractParser.class
            .getResourceAsStream("contracts/prompts/user-prompt.txt"));

        final var contract = readStringFromStream(ContractParser.class
            .getResourceAsStream("contracts/prompts/contract.txt"));

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

    /**
     * Formats a Contract object into a custom, human-readable string.
     * This is the primary public method to call.
     *
     * @param contract The Contract object to format.
     * @return A String containing the custom representation of the contract.
     */
    public static String formatContractCustom(Contract contract) {
        StringBuilder sb = new StringBuilder();

        sb.append("---\n");

        // Format sellers
        sb.append("sellers:\n");
        for (Person seller : contract.sellers()) {
            appendPerson(sb, seller);
        }

        // Format buyers
        sb.append("buyers:\n");
        for (Person buyer : contract.buyers()) {
            appendPerson(sb, buyer);
        }

        sb.append("---");

        return sb.toString();
    }

    /**
     * Helper method to append a formatted Person record to the StringBuilder.
     *
     * @param sb The StringBuilder to append to.
     * @param person The Person object to format.
     */
    private static void appendPerson(StringBuilder sb, Person person) {
        sb.append("- name: ").append(person.name()).append("\n");
        sb.append("  profession: ").append(person.profession()).append("\n");
        sb.append("  address: ").append(formatAddress(person.address())).append("\n");
    }

    /**
     * Helper method to format an Address record into a single comma-separated string.
     *
     * @param address The Address object to flatten.
     * @return A single-line String representation of the address.
     */
    private static String formatAddress(Address address) {
        return String.join(", ",
                address.streetAddress(),
                address.city(),
                address.stateOrRegion()
        );
    }
}
