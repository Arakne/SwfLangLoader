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

package fr.arakne.swflangloader.parser.mapper;

import fr.arakne.swflangloader.parser.Assignation;
import fr.arakne.swflangloader.parser.AssignationParser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Hydrator for an SWF structure using mapped properties
 *
 * @param <T> The target structure type
 */
final public class MapperHydrator<T> {
    final private AssignationParser parser = new AssignationParser();
    final private Map<String, PropertyHydrator<T, ?>> hydrators = new HashMap<>();
    private PropertyHydrator<T, Map<String, Object>> defaultHydrator;

    /**
     * Define the default property (i.e. for undeclared variable) hydrator
     */
    public void setDefaultHydrator(PropertyHydrator<T, Map<String, Object>> defaultHydrator) {
        this.defaultHydrator = defaultHydrator;
    }

    /**
     * Define a property for a simple variable
     *
     * @param varName The SWF variable name
     * @param type The variable type
     * @param hydrator Hydrator to use
     *
     * @param <V> The variable type
     */
    public <V> void declareSimpleProperty(String varName, Class<V> type, PropertyHydrator<T, V> hydrator) {
        parser.declareSimple(varName, type);
        hydrators.put(varName, hydrator);
    }

    /**
     * Define a property for a map variable
     *
     * @param varName The SWF variable name
     * @param keyType The map key type
     * @param type The map value type
     * @param hydrator Hydrator to use
     *
     * @param <V> The variable type
     */
    public <K, V> void declareMapProperty(String varName, Class<K> keyType, Class<V> type, PropertyHydrator<T, Map<K, V>> hydrator) {
        parser.declareMap(varName, keyType, type);
        hydrators.put(varName, hydrator);
    }

    /**
     * Hydrator the target structure using the given action script line
     *
     * @param target The structure to hydrate
     * @param line The action script line
     */
    public void hydrate(T target, String line) {
        Assignation assignation = parser.parseLine(line);

        if (assignation.isNull()) {
            return;
        }

        if (!hydrators.containsKey(assignation.variableName())) {
            hydrateUndeclared(target, assignation);
            return;
        }

        @SuppressWarnings("unchecked")
        PropertyHydrator<T, Object> hydrator = (PropertyHydrator<T, Object>) hydrators.get(assignation.variableName());

        if (!assignation.isAssociative()) {
            hydrator.set(target, assignation.value());
            return;
        }

        @SuppressWarnings("unchecked")
        Map<Object, Object> value = (Map<Object, Object>) hydrator.get(target);

        if (value == null) {
            value = new HashMap<>();
            hydrator.set(target, value);
        }

        value.put(assignation.key(), assignation.value());
    }

    @SuppressWarnings("unchecked")
    private void hydrateUndeclared(T target, Assignation assignation) {
        if (defaultHydrator == null) {
            return;
        }

        Map<String, Object> value = defaultHydrator.get(target);

        if (value == null) {
            value = new HashMap<>();
            defaultHydrator.set(target, value);
        }

        if (!assignation.isAssociative()) {
            value.put(assignation.variableName(), assignation.value());
            return;
        }

        final Object lastValue = value.get(assignation.variableName());
        Map<Object, Object> mapValue;

        if (!(lastValue instanceof Map)) {
            mapValue = new HashMap<>();
            value.put(assignation.variableName(), mapValue);
        } else {
            mapValue = (Map<Object, Object>) lastValue;
        }

        mapValue.put(assignation.key(), assignation.value());
    }

    /**
     * Create the hydrator instance by parsing annotations
     *
     * @param type The structure class
     * @param <T> The structure type
     *
     * @return The hydrator instance
     */
    static public <T> MapperHydrator<T> parseAnnotations(Class<T> type) {
        MapperHydrator<T> hydrator = new MapperHydrator<>();

        Class<?> current = type;

        while (current.getSuperclass() != null) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(SwfDefault.class)) {
                    defineDefaultField(hydrator, field);
                    continue;
                }

                SwfVariable annotation = field.getAnnotation(SwfVariable.class);

                if (annotation != null) {
                    defineSwfField(hydrator, field, annotation);
                }
            }

            current = current.getSuperclass();
        }

        return hydrator;
    }

    private static <T> void defineSwfField(MapperHydrator<T> hydrator, Field field, SwfVariable annotation) {
        final String varName = annotation.value().isEmpty() ? field.getName() : annotation.value();

        if (field.getType().equals(Map.class)) {
            ParameterizedType mapType = (ParameterizedType) field.getGenericType();

            hydrator.declareMapProperty(
                varName,
                (Class<?>) mapType.getActualTypeArguments()[0],
                (Class<?>) mapType.getActualTypeArguments()[1],
                new ReflectionPropertyHydrator<>(field)
            );
        } else {
            hydrator.declareSimpleProperty(varName, field.getType(), new ReflectionPropertyHydrator<>(field));
        }
    }

    private static <T> void defineDefaultField(MapperHydrator<T> hydrator, Field field) {
        if (!field.getType().equals(Map.class)) {
            throw new IllegalArgumentException("Default swf field must be declared as Map<String, Object>");
        }

        hydrator.setDefaultHydrator(new ReflectionPropertyHydrator<>(field));
    }
}
