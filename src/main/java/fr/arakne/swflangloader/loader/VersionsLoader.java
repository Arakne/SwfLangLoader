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

package fr.arakne.swflangloader.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Loader for SWF files version
 */
public interface VersionsLoader {
    /**
     * Load versions for the given language
     *
     * @param baseUrl The base lang URL
     * @param language The language to load
     *
     * @return A map of versions, indexed by lang file name
     *
     * @throws IOException When cannot load the versions file
     */
    public Map<String, Integer> forLanguage(URL baseUrl, String language) throws IOException;
}
