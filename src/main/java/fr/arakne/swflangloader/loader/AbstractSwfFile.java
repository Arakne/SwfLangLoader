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

/**
 * Base Swf file type, used by loader to inject name language and version
 */
abstract public class AbstractSwfFile {
    private String name;
    private String language;
    private int version;

    /**
     * @return The name of the swf file (Following filename [name]_[language]_[version].swf)
     */
    final public String name() {
        return name;
    }

    /**
     * @return The language of the swf file (Following filename [name]_[language]_[version].swf)
     */
    final public String language() {
        return language;
    }

    /**
     * @return The version of the swf file (Following filename [name]_[language]_[version].swf)
     */
    final public int version() {
        return version;
    }

    void setName(String name) {
        this.name = name;
    }

    void setLanguage(String language) {
        this.language = language;
    }

    void setVersion(int version) {
        this.version = version;
    }
}
