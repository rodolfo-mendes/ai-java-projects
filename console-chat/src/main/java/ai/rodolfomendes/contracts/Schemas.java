package ai.rodolfomendes.contracts;

import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;

public final class Schemas {
    private Schemas() {}

    public static JsonObjectSchema contractSchema() {
        return JsonObjectSchema.builder()
            .addProperty("sellers", arrayOfPersonSchema())
            .addProperty("buyers", arrayOfPersonSchema())
            .build();
    }

    public static JsonArraySchema arrayOfPersonSchema() {
        return JsonArraySchema.builder()
            .items(personSchema())
            .build();
    }

    public static JsonObjectSchema personSchema() {
        return JsonObjectSchema.builder()
            .addStringProperty("name")
            .addStringProperty("profession")
            .addProperty("address", addressSchema())
            .build();
    }

    public static JsonObjectSchema addressSchema() {
        return JsonObjectSchema.builder()
            .addStringProperty("streetAddress")
            .addStringProperty("city")
            .addStringProperty("stateOrRegion")
            .build();
    }
}
