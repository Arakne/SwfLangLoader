package fr.arakne.swflangloader.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Map variable assignation
 */
final public class MapAssignationType implements AssignationType {
    final static private Gson GSON = new Gson();

    final private Type keyType;
    final private Class<?> type;

    public MapAssignationType(Type keyType, Class<?> type) {
        this.keyType = keyType;
        this.type = type;
    }

    @Override
    public Assignation parseAssociative(String varName, String key, String value) {
        try {
            return new Assignation(
                varName,
                GSON.fromJson(key, keyType),
                GSON.fromJson(value, type)
            );
        } catch (JsonSyntaxException e) {
            return Assignation.NULL;
        }
    }
}
