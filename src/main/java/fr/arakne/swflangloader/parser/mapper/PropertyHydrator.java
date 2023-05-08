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

/**
 * Base type for hydrate or extract a variable value from a SWF structure
 *
 * @param <T> Type of target object (i.e. SWF structure)
 * @param <V> Type of the SWF variable
 */
interface PropertyHydrator<T, V> {
    /**
     * @param source The structure
     * @return The value of the variable
     */
    public V get(T source);

    /**
     * Set the value of the variable
     *
     * @param target The structure
     * @param value The value
     */
    public void set(T target, V value);
}
