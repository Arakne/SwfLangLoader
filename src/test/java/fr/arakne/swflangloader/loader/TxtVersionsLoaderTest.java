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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TxtVersionsLoaderTest {
    private TxtVersionsLoader loader;
    private URL baseUrl;

    @BeforeEach
    void setUp() throws MalformedURLException {
        loader = new TxtVersionsLoader();
        baseUrl = Paths.get("assets/lang-1-29").toUri().toURL();
    }

    @Test
    void forLanguageSuccess() throws IOException {
        Map<String, Integer> versions = loader.forLanguage(baseUrl, "fr");

        assertEquals(36, versions.size());

        assertEquals(447, versions.get("items"));
        assertEquals(93, versions.get("guilds"));
        assertEquals(117, versions.get("rides"));
        assertEquals(180, versions.get("classes"));
        assertEquals(288, versions.get("crafts"));
        assertEquals(120, versions.get("dungeons"));
        assertEquals(520, versions.get("dialog"));
        assertEquals(266, versions.get("effects"));
        assertEquals(801, versions.get("lang"));
        assertEquals(508, versions.get("npc"));
        assertEquals(112, versions.get("emotes"));
        assertEquals(25, versions.get("fightChallenge"));
        assertEquals(350, versions.get("spells"));
        assertEquals(18, versions.get("scripts"));
        assertEquals(85, versions.get("speakingitems"));
        assertEquals(147, versions.get("alignment"));
        assertEquals(110, versions.get("names"));
        assertEquals(206, versions.get("states"));
        assertEquals(27, versions.get("subtitles"));
        assertEquals(284, versions.get("skills"));
        assertEquals(366, versions.get("maps"));
        assertEquals(116, versions.get("hints"));
        assertEquals(129, versions.get("houses"));
        assertEquals(198, versions.get("interactiveobjects"));
        assertEquals(180, versions.get("itemsets"));
        assertEquals(143, versions.get("jobs"));
        assertEquals(108, versions.get("ranks"));
        assertEquals(103, versions.get("pvp"));
        assertEquals(291, versions.get("servers"));
        assertEquals(228, versions.get("shortcuts"));
        assertEquals(102, versions.get("kb"));
        assertEquals(369, versions.get("monsters"));
        assertEquals(411, versions.get("quests"));
        assertEquals(7, versions.get("titles"));
        assertEquals(99, versions.get("timezones"));
        assertEquals(56, versions.get("audio"));
    }
}
