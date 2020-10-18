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

package fr.arakne.swflangloader.lang.lang;

import fr.arakne.swflangloader.lang.BaseLangFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The lang_xx_xxx.swf file
 * Contains translations and global configurations
 */
final public class LangFile extends BaseLangFile {
    final static private MapperHydrator<LangFile> HYDRATOR = MapperHydrator.parseAnnotations(LangFile.class);

    public LangFile(URL file, SwfFileLoader loader) throws IOException, InterruptedException {
        loader.load(file, this, HYDRATOR);
    }

    public LangFile(URL file) throws IOException, InterruptedException {
        this(file, new SwfFileLoader());
    }

    public LangFile(File file) throws IOException, InterruptedException {
        this(file.toURI().toURL(), new SwfFileLoader());
    }

    /**
     * Get an error message (packet Im1)
     *
     * @param id The error message id
     *
     * @return The message
     */
    public String error(int id) {
        return string("ERROR_" + id);
    }

    /**
     * Get an info message (packet Im0)
     *
     * @param id The info message id
     *
     * @return The message
     */
    public String info(int id) {
        return string("INFOS_" + id);
    }

    /**
     * Get an pvp message (packet Im0)
     *
     * @param id The pvp message id
     *
     * @return The message
     */
    public String pvp(int id) {
        return string("PVP_" + id);
    }
}
