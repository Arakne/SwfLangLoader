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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple variable assignation
 */
final public class SimpleAssignationType implements AssignationType {
    final static private Logger LOGGER = LoggerFactory.getLogger(AssignationParser.class);
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
            LOGGER.warn("[SWF] Cannot parse {} as JSON: {} (variable: {})", value, e.getMessage(), varName);
            return Assignation.NULL;
        }
    }
}
