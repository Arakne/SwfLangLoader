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

import java.util.Objects;

/**
 * DTO for action script assignation operation
 */
final public class Assignation {
    final static public Assignation NULL = new Assignation(null, null, null);

    final private String variableName;
    final private Object key;
    final private Object value;

    public Assignation(String variableName, Object key, Object value) {
        this.variableName = variableName;
        this.key = key;
        this.value = value;
    }

    /**
     * @return The assigned variable name
     */
    public String variableName() {
        return variableName;
    }

    /**
     * @return The assigned object key. Null if not associative assignation
     */
    public Object key() {
        return key;
    }

    /**
     * @return The assigned value
     */
    public Object value() {
        return value;
    }

    /**
     * @return true if it's a null assignation (must be skipped)
     */
    public boolean isNull() {
        return variableName == null;
    }

    /**
     * @return true if it's an associative assignation
     */
    public boolean isAssociative() {
        return key != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignation that = (Assignation) o;
        return Objects.equals(variableName, that.variableName) &&
            Objects.equals(key, that.key) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName, key, value);
    }

    /**
     * Create a simple (i.e. not associative) assignation
     *
     * @param varName The assigned variable
     * @param value The assigned value
     *
     * @return The assignation
     */
    static public Assignation simple(String varName, Object value) {
        return new Assignation(varName, null, value);
    }
}
