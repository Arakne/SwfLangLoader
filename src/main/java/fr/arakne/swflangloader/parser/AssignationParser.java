/*
 * This file is part of ArakneLangLoader.
 *
 * ArakneLangLoader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArakneLangLoader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ArakneLangLoader.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2020 Vincent Quatrevieux
 */

package fr.arakne.swflangloader.parser;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Parse action script assignations
 * Some variable type can be declared
 */
final public class AssignationParser {
    final static private String[] CAST_FUNCTIONS = new String[] {"Boolean", "String", "Number"};

    final private Map<String, AssignationType> declaredTypes = new HashMap<>();
    final private AssignationType defaultType = new DefaultAssignationType();

    /**
     * Declare a variable
     *
     * @param varName The variable name
     * @param type The variable type
     *
     * @return this
     */
    public AssignationParser declare(String varName, AssignationType type) {
        declaredTypes.put(varName, type);

        return this;
    }

    /**
     * Declare a simple variable (not associative)
     *
     * @param varName The variable name
     * @param type The variable type
     *
     * @return this
     */
    public AssignationParser declareSimple(String varName, Class<?> type) {
        return declare(varName, new SimpleAssignationType(type));
    }

    /**
     * Declare a map variable (associative)
     *
     * @param varName The variable name
     * @param keyType The map key type
     * @param type The map value type
     *
     * @return this
     */
    public AssignationParser declareMap(String varName, Type keyType, Class<?> type) {
        return declare(varName, new MapAssignationType(keyType, type));
    }

    /**
     * Declare an integer map variable
     *
     * @param varName The variable name
     * @param type The map value type
     *
     * @return this
     */
    public AssignationParser declareIntegerMap(String varName, Class<?> type) {
        return declareMap(varName, Integer.class, type);
    }

    /**
     * Declare an string map variable
     *
     * @param varName The variable name
     * @param type The map value type
     *
     * @return this
     */
    public AssignationParser declareStringMap(String varName, Class<?> type) {
        return declareMap(varName, String.class, type);
    }

    /**
     * Parse an action script line to get the assignation
     *
     * @param line Line to parse
     *
     * @return The assignation
     */
    public Assignation parseLine(String line) {
        if (!line.endsWith(";")) {
            return Assignation.NULL;
        }

        final int eqPos = line.indexOf('=');

        if (eqPos == -1) {
            return Assignation.NULL;
        }

        final String left = line.substring(0, eqPos).trim();
        final String right = handleConcatenation(
            removeCastFunctions(
                line.substring(eqPos + 1, line.length() - 1).trim()
            )
        );

        if (left.isEmpty() || right.isEmpty()) {
            return Assignation.NULL;
        }

        if (left.endsWith("]")) {
            return parseAssoc(left, right);
        } else {
            return parseSimple(left, right);
        }
    }

    private Assignation parseAssoc(String left, String right) {
        final int assocOpen = left.indexOf('[');

        if (assocOpen == -1) {
            return Assignation.NULL;
        }

        final String varName = left.substring(0, assocOpen);
        final String key = left.substring(assocOpen + 1, left.length() - 1);

        return declaredTypes.getOrDefault(varName, defaultType).parseAssociative(varName, key, right);
    }

    private Assignation parseSimple(String varName, String value) {
        return declaredTypes.getOrDefault(varName, defaultType).parseSimple(varName, value);
    }

    private String removeCastFunctions(String value) {
        for (String func : CAST_FUNCTIONS) {
            if (value.startsWith(func) && value.charAt(func.length()) == '(' && value.charAt(value.length() - 1) == ')') {
                value = value.substring(func.length() + 1, value.length() - 1);
            }
        }

        return value;
    }

    /**
     * Remove simple string concatenation
     * See: https://github.com/Arakne/SwfLangLoader/issues/1
     */
    private String handleConcatenation(String value) {
        return value.replaceAll("\" \\+ \"", "");
    }
}
