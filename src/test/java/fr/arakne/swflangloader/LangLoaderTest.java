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

package fr.arakne.swflangloader;

import com.google.gson.JsonPrimitive;
import fr.arakne.swflangloader.lang.BaseLangFile;
import fr.arakne.swflangloader.lang.maps.MapsFile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LangLoaderTest {
    static private LangLoader loader;

    @BeforeAll
    static void beforeAll() throws MalformedURLException {
        loader = new LangLoader(Paths.get("assets/lang-1-29").toUri().toURL(), "fr");
    }

    @AfterAll
    static void afterAll() {
        loader = null;
    }

    @Test
    void load() throws IOException, InterruptedException {
        BaseLangFile file = loader.load("alignment");

        assertEquals("alignment", file.name());
        assertEquals(147, file.version());
        assertEquals("fr", file.language());

        assertEquals(new JsonPrimitive("Neutre"), file.assoc("A.a").json("0").getAsJsonObject().get("n"));
        assertEquals(new JsonPrimitive("Brakmarien"), file.assoc("A.a").json("2").getAsJsonObject().get("n"));

        assertSame(file, loader.load("alignment"));
        assertEquals("audio", loader.load("audio").name());
        assertEquals(56, loader.load("audio").version());

        assertThrows(NoSuchElementException.class, () -> loader.load("not_found"));
    }

    @Test
    void maps() throws IOException, InterruptedException {
        assertEquals("maps", loader.maps().name());
        assertEquals("fr", loader.maps().language());
        assertEquals(366, loader.maps().version());
        assertSame(loader.maps(), loader.maps());
        assertEquals(-26, loader.maps().position(5989).x());
    }

    @Test
    void classes() throws IOException, InterruptedException {
        assertEquals("classes", loader.classes().name());
        assertEquals("fr", loader.classes().language());
        assertEquals(180, loader.classes().version());
        assertSame(loader.classes(), loader.classes());
        assertEquals("Enutrof", loader.classes().get(3).shortName());
    }

    @Test
    void hints() throws IOException, InterruptedException {
        assertEquals("hints", loader.hints().name());
        assertEquals("fr", loader.hints().language());
        assertEquals(116, loader.hints().version());
        assertSame(loader.hints(), loader.hints());
        assertEquals("Green", loader.hints().category(3).color());
    }

    @Test
    void lang() throws IOException, InterruptedException {
        assertEquals("lang", loader.lang().name());
        assertEquals("fr", loader.lang().language());
        assertEquals(801, loader.lang().version());
        assertSame(loader.lang(), loader.lang());
        assertEquals("Pour des raisons de maintenances, le serveur va être redémarré dans %1.", loader.lang().error(15));
    }

    @Test
    void clear() throws IOException, InterruptedException {
        MapsFile maps = loader.maps();

        assertSame(maps, loader.maps());
        loader.clear();
        assertNotSame(maps, loader.maps());
    }
}
