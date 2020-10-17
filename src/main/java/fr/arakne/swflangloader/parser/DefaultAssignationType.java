package fr.arakne.swflangloader.parser;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Default (i.e. undeclared) assignation
 * Will parse as JsonElement
 */
final public class DefaultAssignationType implements AssignationType {
    @Override
    public Assignation parseSimple(String varName, String value) {
        try {
            return Assignation.simple(varName, JsonParser.parseString(value));
        } catch (JsonSyntaxException e) {
            return Assignation.NULL;
        }
    }

    @Override
    public Assignation parseAssociative(String varName, String key, String value) {
        try {
            return new Assignation(
                varName,
                JsonParser.parseString(key).getAsString(),
                JsonParser.parseString(value)
            );
        } catch (JsonSyntaxException e) {
            return Assignation.NULL;
        }
    }
}
