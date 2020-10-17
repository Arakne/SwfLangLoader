package fr.arakne.swflangloader.parser;

/**
 * Assignation type
 */
public interface AssignationType {
    /**
     * Parse a simple assignation
     *
     * @param varName The assigned variable name
     * @param value The assigned raw value
     *
     * @return The assignation
     */
    default public Assignation parseSimple(String varName, String value) {
        return Assignation.NULL;
    }

    /**
     * Parse a map assignation
     *
     * @param varName The assigned variable name
     * @param key The assignation raw key
     * @param value The assigned raw value
     *
     * @return The assignation
     */
    default public Assignation parseAssociative(String varName, String key, String value) {
        return Assignation.NULL;
    }
}
