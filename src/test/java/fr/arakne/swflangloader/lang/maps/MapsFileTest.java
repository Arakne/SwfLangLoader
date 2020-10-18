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

package fr.arakne.swflangloader.lang.maps;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class MapsFileTest {
    static private MapsFile maps;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        maps = new MapsFile(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/maps_fr_366.swf"));
    }

    @AfterAll
    static void afterAll() {
        maps = null;
    }

    @Test
    void mapPosition() {
        MapPosition position = maps.position(5769);

        assertEquals(5769, position.id());
        assertEquals(-31, position.x());
        assertEquals(-56, position.y());
        assertEquals(16, position.ep());
        assertEquals(50, position.subAreaId());
        assertEquals("Quartier des Tailleurs", position.subArea().name());
        assertEquals("Bonta", position.subArea().area().name());
        assertEquals("Continent Amaknien", position.subArea().area().superArea());

        assertEquals(9193, maps.allMapPositions().size());

        Collection<MapPosition> positions = maps.positions(3, -5);

        assertEquals(5, positions.size());
        assertArrayEquals(new int[] {9454, 9680, 9681, 9682, 9683}, positions.stream().mapToInt(MapPosition::id).toArray());
        assertTrue(positions.stream().anyMatch(pos -> pos.x() == 3 && pos.y() == -5));
        assertThrows(UnsupportedOperationException.class, () -> positions.add(new MapPosition()));

        assertEquals(0, maps.positions(-100, 123).size());
        assertNull(maps.position(-404));

        assertEquals(maps.position(1125), maps.position(1125));
        assertEquals(maps.position(1125).hashCode(), maps.position(1125).hashCode());
        assertNotEquals(maps.position(1125), maps.position(1451));
        assertNotEquals(maps.position(1125).hashCode(), maps.position(1451).hashCode());

        assertEquals("MapPosition{5769, x=-31, y=-56, sa=Quartier des Tailleurs (50)}", position.toString());

        assertEquals(15, maps.position(1125).distance(maps.position(1451)));
        assertEquals(15, maps.position(1451).distance(maps.position(1125)));
        assertEquals(0, maps.position(1125).distance(maps.position(1125)));
        assertEquals(8, maps.position(1125).distance(maps.position(1127)));
    }

    @Test
    void subAreas() {
        MapSubArea sa = maps.subArea(515);

        assertEquals(515, sa.id());
        assertEquals(11, sa.areaId());
        assertEquals("Brâkmar", sa.area().name());
        assertArrayEquals(new Integer[] {31}, sa.musics());
        assertArrayEquals(new int[] {61, 72, 75}, sa.adjacentSubAreaIds());
        assertEquals("Tour des ordres", sa.name());
        assertArrayEquals(new MapSubArea[] {maps.subArea(61), maps.subArea(72), maps.subArea(75)}, sa.adjacentSubAreas());

        assertEquals(370, maps.allSubAreas().size());
        assertNull(maps.subArea(-404));

        assertEquals(maps.subArea(45), maps.subArea(45));
        assertEquals(maps.subArea(45).hashCode(), maps.subArea(45).hashCode());
        assertNotEquals(maps.subArea(45), maps.subArea(75));
        assertNotEquals(maps.subArea(45).hashCode(), maps.subArea(75).hashCode());

        assertEquals("MapSubArea{Tour des ordres (515), area=Brâkmar (11)}", sa.toString());

        assertTrue(sa.isAdjacent(maps.subArea(75)));
        assertFalse(sa.isAdjacent(maps.subArea(45)));

        assertEquals(3, maps.subArea(449).maps().size());
        assertArrayEquals(new int[] {10340, 10341, 10342}, maps.subArea(449).maps().stream().mapToInt(MapPosition::id).toArray());
    }

    @Test
    void areas() {
        MapArea area = maps.area(18);

        assertEquals(18, area.id());
        assertEquals("Astrub", area.name());
        assertEquals(0, area.superAreaId());
        assertEquals("Continent Amaknien", area.superArea());

        assertEquals(46, maps.allAreas().size());
        assertNull(maps.area(-404));

        assertEquals(maps.area(5), maps.area(5));
        assertEquals(maps.area(5).hashCode(), maps.area(5).hashCode());
        assertNotEquals(maps.area(5), maps.area(15));
        assertNotEquals(maps.area(5).hashCode(), maps.area(15).hashCode());

        assertEquals("MapArea{Astrub (18)}", area.toString());
    }

    @Test
    void superAreas() {
        assertEquals("Continent Amaknien", maps.superArea(0));
        assertNull(maps.superArea(1));
        assertEquals("Zone de départ", maps.superArea(3));
    }
}
