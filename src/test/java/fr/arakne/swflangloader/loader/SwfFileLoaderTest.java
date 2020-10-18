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

import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfVariable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SwfFileLoaderTest {
    private SwfFileLoader loader;

    static public class Structure extends AbstractSwfFile {
        @SwfVariable
        public int VERSION;
        @SwfVariable
        public boolean FILE_BEGIN;
        @SwfVariable
        public String ACCEPT;
        @SwfVariable
        public boolean FILE_END;
    }

    @BeforeEach
    void setUp() {
        loader = new SwfFileLoader();
    }

    @AfterEach
    void tearDown() throws IOException {
        loader.clear();
    }

    @Test
    void load() throws IOException, InterruptedException {
        Structure structure = new Structure();
        loader.load(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/lang_fr_801.swf"), structure, MapperHydrator.parseAnnotations(Structure.class));

        assertEquals(801, structure.VERSION);
        assertTrue(structure.FILE_BEGIN);
        assertTrue(structure.FILE_END);
        assertEquals("Accepter", structure.ACCEPT);

        assertEquals("lang", structure.name());
        assertEquals("fr", structure.language());
        assertEquals(801, structure.version());

        assertTrue(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));
    }

    @Test
    void loadWithoutCache() throws IOException, InterruptedException {
        Structure structure = new Structure();
        new SwfFileLoader(Paths.get("./tmp"), false).load(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/lang_fr_801.swf"), structure, MapperHydrator.parseAnnotations(Structure.class));

        assertEquals(801, structure.VERSION);
        assertTrue(structure.FILE_BEGIN);
        assertTrue(structure.FILE_END);
        assertEquals("Accepter", structure.ACCEPT);

        assertEquals("lang", structure.name());
        assertEquals("fr", structure.language());
        assertEquals(801, structure.version());

        assertFalse(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));
    }

    @Test
    void clear() throws IOException, InterruptedException {
        Structure structure = new Structure();
        loader.load(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/lang_fr_801.swf"), structure, MapperHydrator.parseAnnotations(Structure.class));

        assertTrue(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));
        loader.clear();
        assertFalse(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));
        assertFalse(Files.exists(Paths.get("./tmp")));
    }
}
