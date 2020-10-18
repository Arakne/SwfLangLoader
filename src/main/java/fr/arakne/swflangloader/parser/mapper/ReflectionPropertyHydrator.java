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

import java.lang.reflect.Field;

/**
 * Hydrator using reflection field
 */
final public class ReflectionPropertyHydrator<T, V> implements PropertyHydrator<T, V> {
    final private Field field;

    public ReflectionPropertyHydrator(Field field) {
        this.field = field;

        field.setAccessible(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(T source) {
        try {
            return (V) field.get(source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // Should not occurs
        }
    }

    @Override
    public void set(T target, V value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // Should not occurs
        }
    }
}
