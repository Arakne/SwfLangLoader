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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ClassesFileTest {
    static private ClassesFile classes;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        classes = new ClassesFile(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/classes_fr_180.swf"));
    }

    @AfterAll
    static void afterAll() {
        classes = null;
    }

    @Test
    void filename() {
        assertEquals("classes", classes.name());
        assertEquals("fr", classes.language());
        assertEquals(180, classes.version());
    }

    @Test
    void all() {
        assertEquals(12, classes.all().size());
    }

    @Test
    void get() {
        DofusClass dofusClass = classes.get(1);

        assertEquals(1, dofusClass.id());
        assertEquals("Féca", dofusClass.shortName());
        assertEquals("Le bouclier Féca", dofusClass.longName());
        assertEquals("Les Fecas sont des Protecteurs loyaux. Ils sont appréciés dans les groupes d'aventuriers à cause de leurs pouvoirs de protection, mais aussi parce qu'ils manient le bâton comme pas deux. Des générations de Fécas s'enorgueillissent d'être des protecteurs hors pair à qui les Sages ont confié les objets les plus précieux qui soient, en toute confiance.", dofusClass.description());
        assertArrayEquals(new int[] {3,17,6,4,2,1,9,18,20,14,19,5,16,8,12,11,10,7,15,13,1901}, dofusClass.spellIds());
        assertEquals(11, dofusClass.closeCombatData().length);

        assertEquals(3, dofusClass.strengthBoost().at(125).cost());
        assertEquals(1, dofusClass.vitalityBoost().at(125).cost());
        assertEquals(3, dofusClass.wisdomBoost().at(125).cost());
        assertEquals(5, dofusClass.luckBoost().at(125).cost());
        assertEquals(5, dofusClass.agilityBoost().at(125).cost());
        assertEquals(2, dofusClass.intelligenceBoost().at(125).cost());
    }

    @Test
    void equalsAndHash() {
        assertEquals(classes.get(1), classes.get(1));
        assertEquals(classes.get(1).hashCode(), classes.get(1).hashCode());
        assertNotEquals(classes.get(2), classes.get(1));
        assertNotEquals(classes.get(2).hashCode(), classes.get(1).hashCode());
    }

    @Test
    void string() {
        assertEquals("DofusClass{name=Féca, id=1}", classes.get(1).toString());
    }
}
