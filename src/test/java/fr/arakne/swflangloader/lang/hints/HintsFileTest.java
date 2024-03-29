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

package fr.arakne.swflangloader.lang.hints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HintsFileTest {
    static private HintsFile hints;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        hints = new HintsFile(Paths.get("assets/lang-1-29/swf/hints_fr_116.swf").toUri().toURL());
    }

    @AfterAll
    static void afterAll() {
        hints = null;
    }

    @Test
    void filename() {
        assertEquals("hints", hints.name());
        assertEquals("fr", hints.language());
        assertEquals(116, hints.version());
    }

    @Test
    void hints() {
        assertEquals(318, hints.stream().count());

        int count = 0;

        for (Hint hint : hints) {
            assertNotNull(hint);

            assertSame(hint.category(), hints.category(hint.categoryId()));
            ++count;
        }

        assertEquals(318, count);

        AtomicInteger aCount = new AtomicInteger();
        hints.forEach(hint -> aCount.incrementAndGet());
        assertEquals(318, aCount.get());

        List<Hint> hintList = hints.stream().collect(Collectors.toList());

        assertEquals(hintList.get(5), hintList.get(5));
        assertEquals(hintList.get(5).hashCode(), hintList.get(5).hashCode());
        assertNotEquals(hintList.get(5), hintList.get(8));
        assertNotEquals(hintList.get(5).hashCode(), hintList.get(8).hashCode());

        assertEquals("Hint{Temple Crâ (90), map=540}", hintList.get(5).toString());
    }

    @Test
    void byType() {
        assertEquals(8, hints.byType(Hint.TYPE_BANK).size());

        for (Hint hint : hints.byType(Hint.TYPE_BANK)) {
            assertEquals("Banque", hint.name());
            assertEquals(Hint.TYPE_BANK, hint.type());
            assertTrue(hint.is(Hint.TYPE_BANK));
            assertFalse(hint.is(Hint.TYPE_ARENA));
            assertEquals(4, hint.categoryId());
            assertEquals("Divers", hint.category().name());
            assertEquals("Beige", hint.category().color());
        }

        assertEquals(1, hints.byType(Hint.TYPE_MOON_CANNON).size());
        for (Hint hint : hints.byType(Hint.TYPE_MOON_CANNON)) {
            assertEquals(1014, hint.mapId());
        }
    }

    @Test
    void category() {
        assertEquals(1, hints.category(1).id());
        assertEquals("Lieux de classes", hints.category(1).name());
        assertEquals("Orange", hints.category(1).color());

        assertEquals(3, hints.category(3).id());
        assertEquals("Ateliers", hints.category(3).name());
        assertEquals("Green", hints.category(3).color());

        assertSame(hints.category(1), hints.category(1));

        assertEquals(hints.category(1), hints.category(1));
        assertEquals(hints.category(1).hashCode(), hints.category(1).hashCode());
        assertNotEquals(hints.category(1), hints.category(2));
        assertNotEquals(hints.category(1).hashCode(), hints.category(2).hashCode());

        assertEquals("HintCategory{Lieux de classes (1)}", hints.category(1).toString());
    }
}
