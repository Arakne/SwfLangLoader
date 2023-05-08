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

import com.github.valfirst.slf4jtest.Assertions;
import com.github.valfirst.slf4jtest.LoggingEvent;
import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfVariable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jext.Logger;
import uk.org.lidalia.slf4jext.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SwfFileLoaderTest {
    private final TestLogger logger = TestLoggerFactory.getTestLogger(SwfFileLoader.class);

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
        logger.clearAll();
        loader = new SwfFileLoader();
    }

    @AfterEach
    void tearDown() throws IOException {
        loader.clear();
    }

    @Test
    void load() throws IOException, InterruptedException {
        Structure structure = new Structure();
        loader.load(Paths.get("assets/lang-1-29/swf/lang_fr_801.swf").toUri().toURL(), structure, MapperHydrator.parseAnnotations(Structure.class));

        assertEquals(801, structure.VERSION);
        assertTrue(structure.FILE_BEGIN);
        assertTrue(structure.FILE_END);
        assertEquals("Accepter", structure.ACCEPT);

        assertEquals("lang", structure.name());
        assertEquals("fr", structure.language());
        assertEquals(801, structure.version());

        assertTrue(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));

        List<LoggingEvent> logs = logger.getLoggingEvents();

        assertEquals(2, logs.size());
        assertEquals(Level.DEBUG, logs.get(0).getLevel());
        assertEquals("[SWF] Loading {} to {}", logs.get(0).getMessage());
        assertTrue(logs.get(0).getArguments().get(0).toString().endsWith("assets/lang-1-29/swf/lang_fr_801.swf"));
        assertEquals("Structure", logs.get(0).getArguments().get(1).toString());

        assertEquals(Level.DEBUG, logs.get(1).getLevel());
        assertEquals("[SWF] {} is not cached. Load from SWF", logs.get(1).getMessage());
        assertTrue(logs.get(1).getArguments().get(0).toString().endsWith("assets/lang-1-29/swf/lang_fr_801.swf"));

        logger.clearAll();
        // Should be loaded from cache
        loader.load(Paths.get("assets/lang-1-29/swf/lang_fr_801.swf").toUri().toURL(), structure, MapperHydrator.parseAnnotations(Structure.class));
        logs = logger.getLoggingEvents();

        assertEquals(1, logs.size());
        assertEquals("[SWF] Loading {} to {}", logs.get(0).getMessage());
    }

    @Test
    void loadWithoutCache() throws IOException, InterruptedException {
        Structure structure = new Structure();
        SwfFileLoader loader = new SwfFileLoader(Paths.get("./tmp"), false);
        loader.load(Paths.get("assets/lang-1-29/swf/lang_fr_801.swf").toUri().toURL(), structure, MapperHydrator.parseAnnotations(Structure.class));

        assertEquals(801, structure.VERSION);
        assertTrue(structure.FILE_BEGIN);
        assertTrue(structure.FILE_END);
        assertEquals("Accepter", structure.ACCEPT);

        assertEquals("lang", structure.name());
        assertEquals("fr", structure.language());
        assertEquals(801, structure.version());

        assertFalse(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));

        List<LoggingEvent> logs = logger.getLoggingEvents();

        assertEquals(2, logs.size());
        assertEquals("[SWF] Loading {} to {}", logs.get(0).getMessage());
        assertEquals("[SWF] {} is not cached. Load from SWF", logs.get(1).getMessage());

        logger.clearAll();
        loader.load(Paths.get("assets/lang-1-29/swf/lang_fr_801.swf").toUri().toURL(), structure, MapperHydrator.parseAnnotations(Structure.class));
        logs = logger.getLoggingEvents();

        // Cache is ignored
        assertEquals(2, logs.size());
        assertEquals("[SWF] Loading {} to {}", logs.get(0).getMessage());
        assertEquals("[SWF] {} is not cached. Load from SWF", logs.get(1).getMessage());
    }

    @Test
    void clear() throws IOException, InterruptedException {
        Structure structure = new Structure();
        loader.load(Paths.get("assets/lang-1-29/swf/lang_fr_801.swf").toUri().toURL(), structure, MapperHydrator.parseAnnotations(Structure.class));

        assertTrue(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));
        loader.clear();
        assertFalse(Files.exists(Paths.get("./tmp/lang_fr_801/frame_1/DoAction.as")));
        assertFalse(Files.exists(Paths.get("./tmp")));
    }
}
