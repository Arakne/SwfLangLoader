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

package fr.arakne.swflangloader.lang;

import com.google.gson.JsonElement;
import fr.arakne.swflangloader.loader.AbstractSwfFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfDefault;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Simple lang file structure
 * Forward all variable to a default field
 */
public class BaseLangFile extends AbstractSwfFile implements JsonMapGetter {
    final static private MapperHydrator<BaseLangFile> HYDRATOR = MapperHydrator.parseAnnotations(BaseLangFile.class);

    @SwfDefault
    final protected Map<String, Object> values = new HashMap<>();

    final static public class AssocVariable implements JsonMapGetter {
        final private String varName;
        final private Map<String, JsonElement> assoc;

        public AssocVariable(String varName, Map<String, JsonElement> assoc) {
            this.varName = varName;
            this.assoc = assoc;
        }

        @Override
        public JsonElement json(String variable) {
            if (!assoc.containsKey(variable)) {
                throw new NoSuchElementException("The variable " + varName + "[" + variable + "] is not found");
            }

            return assoc.get(variable);
        }

        /**
         * @return All elements
         */
        public Map<String, JsonElement> all() {
            return Collections.unmodifiableMap(assoc);
        }
    }

    /**
     * Get a raw JSON element value
     *
     * @param variable The variable name
     *
     * @return The variable value
     */
    final public JsonElement json(String variable) {
        final Object value = values.get(variable);

        if (value instanceof JsonElement) {
            return (JsonElement) value;
        }

        throw new NoSuchElementException("Variable " + variable + " is not found or not a valid JSON value");
    }

    /**
     * Get an associative variable
     *
     * @param variableName The variable name
     *
     * @return The associative value
     */
    @SuppressWarnings("unchecked")
    final public AssocVariable assoc(String variableName) {
        final Object value = values.get(variableName);

        if (value instanceof Map) {
            return new AssocVariable(variableName, (Map<String, JsonElement>) value);
        }

        throw new NoSuchElementException("Variable " + variableName + " is not found or not a valid assoc value");
    }

    static public BaseLangFile load(URL url, SwfFileLoader loader) throws IOException, InterruptedException {
        BaseLangFile file = new BaseLangFile();

        loader.load(url, file, HYDRATOR);

        return file;
    }
}
