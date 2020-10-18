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
