package fr.arakne.swflangloader.lang;

import com.google.gson.JsonElement;

import java.util.Optional;

/**
 * Get a JSON value from the swf structure
 */
public interface JsonMapGetter {
    /**
     * Get a string lang value
     *
     * @param variable The variable name
     *
     * @return The lang value as string
     * @throws java.util.NoSuchElementException When variable is not found
     */
    default public String string(String variable) {
        return Optional.ofNullable(json(variable)).map(JsonElement::getAsString).orElse(null);
    }

    /**
     * Get a integer lang value
     *
     * @param variable The variable name
     *
     * @return The lang value as integer
     * @throws java.util.NoSuchElementException When variable is not found
     */
    default public int integer(String variable) {
        return json(variable).getAsInt();
    }

    /**
     * Get a decimal lang value
     *
     * @param variable The variable name
     *
     * @return The lang value as double
     * @throws java.util.NoSuchElementException When variable is not found
     */
    default public double decimal(String variable) {
        return json(variable).getAsDouble();
    }

    /**
     * Get a boolean lang value
     *
     * @param variable The variable name
     *
     * @return The lang value as boolean
     * @throws java.util.NoSuchElementException When variable is not found
     */
    default public boolean bool(String variable) {
        return json(variable).getAsBoolean();
    }

    /**
     * Get a raw JSON element value
     *
     * @param variable The variable name
     *
     * @return The variable value
     * @throws java.util.NoSuchElementException When variable is not found
     */
    public JsonElement json(String variable);
}
