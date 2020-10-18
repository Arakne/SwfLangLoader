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

package fr.arakne.swflangloader.lang.classes;

import fr.arakne.swflangloader.loader.AbstractSwfFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfVariable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Store classes data
 */
final public class ClassesFile extends AbstractSwfFile {
    final static private MapperHydrator<ClassesFile> HYDRATOR = MapperHydrator.parseAnnotations(ClassesFile.class);

    @SwfVariable("G")
    final private Map<Integer, DofusClass> classes = new HashMap<>();

    public ClassesFile(URL file, SwfFileLoader loader) throws IOException, InterruptedException {
        loader.load(file, this, HYDRATOR);
        init();
    }

    public ClassesFile(URL file) throws IOException, InterruptedException {
        this(file, new SwfFileLoader());
    }

    public ClassesFile(File file) throws IOException, InterruptedException {
        this(file.toURI().toURL(), new SwfFileLoader());
    }

    /**
     * @return All available classes
     */
    public Collection<DofusClass> all() {
        return classes.values();
    }

    /**
     * Get a class by its id
     *
     * @param id The class id
     *
     * @return The class data
     */
    public DofusClass get(int id) {
        return classes.get(id);
    }

    private void init() {
        classes.forEach((id, dofusClass) -> dofusClass.setId(id));
    }
}
