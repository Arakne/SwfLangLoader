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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LangFileTest {
    static private LangFile lang;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        lang = new LangFile(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/lang_fr_801.swf"));
    }

    @AfterAll
    static void afterAll() {
        lang = null;
    }

    @Test
    void filename() {
        assertEquals("lang", lang.name());
        assertEquals("fr", lang.language());
        assertEquals(801, lang.version());
    }

    @Test
    void string() {
        assertEquals("Sélectionne d'abord le prix d'un lot.", lang.string("SELECT_SET_PRICE"));
        assertEquals("#3/#2/#1 #4{~4:}#5", lang.string("C.DATE_FORMAT"));
        assertThrows(NoSuchElementException.class, () -> lang.string("undefined"));
    }

    @Test
    void assoc() {
        assertEquals(4, lang.assoc("ABR").all().size());
        assertTrue(lang.assoc("ABR").json("3") instanceof JsonObject);
        assertEquals("Vente de code Audiotel", ((JsonObject) lang.assoc("ABR").json("3")).get("t").getAsString());
        assertThrows(NoSuchElementException.class, () -> lang.assoc("ZOOM"));
        assertThrows(NoSuchElementException.class, () -> lang.assoc("undefined"));
    }

    @Test
    void json() {
        assertTrue(lang.json("C.SERVER_PORT") instanceof JsonArray);
        assertEquals(443, ((JsonArray) lang.json("C.SERVER_PORT")).get(0).getAsInt());
        assertEquals(5555, ((JsonArray) lang.json("C.SERVER_PORT")).get(1).getAsInt());
    }

    @Test
    void integer() {
        assertEquals(801, lang.integer("VERSION"));
        assertThrows(NoSuchElementException.class, () -> lang.string("undefined"));
    }

    @Test
    void decimal() {
        assertEquals(0.1, lang.decimal("C.SELL_PRICE_MULTIPLICATOR"));
    }

    @Test
    void bool() {
        assertTrue(lang.bool("C.SERVER_LIST_USE_FIND_FRIEND"));
        assertFalse(lang.bool("C.SHOW_PVP_GAIN_WARNING_POPUP"));
    }

    @Test
    void error() {
        assertEquals("L'heure actuelle ne convient pas.", lang.error(18));
    }

    @Test
    void info() {
        assertEquals("Tout le monde sera autorisé à rejoindre vos prochains combats.", lang.info(102));
    }

    @Test
    void pvp() {
        assertEquals("Le coeur de la zone %1 est ouvert aux ennemis, protégez le !", lang.pvp(87));
    }
}
