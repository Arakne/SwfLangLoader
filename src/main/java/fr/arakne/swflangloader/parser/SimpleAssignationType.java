package fr.arakne.swflangloader.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Simple variable assignation
 */
final public class SimpleAssignationType implements AssignationType {
    final static private Gson GSON = new Gson();

    final private Class<?> type;

    public SimpleAssignationType(Class<?> type) {
        this.type = type;
    }

    @Override
    public Assignation parseSimple(String varName, String value) {
        try {
            return Assignation.simple(varName, GSON.fromJson(value, type));
        } catch (JsonSyntaxException e) {
            return Assignation.NULL;
        }
    }
}
