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

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Dofus map sub area
 */
final public class MapSubArea {
    private int id;
    private MapsFile maps;

    private String n;
    private int a;
    private Integer[] m;
    private int[] v;

    /**
     * @return The sub area id. Unique on the maps file
     */
    public int id() {
        return id;
    }

    /**
     * @return The sub area name
     */
    public String name() {
        return n;
    }

    /**
     * @return The parent area id
     */
    public int areaId() {
        return a;
    }

    /**
     * @return The parent area instance
     */
    public MapArea area() {
        return maps.area(a);
    }

    /**
     * @return Available musics. May contains null elements
     */
    public Integer[] musics() {
        return m;
    }

    /**
     * @return Array of adjacent (i.e. accessible) sub areas ids
     */
    public int[] adjacentSubAreaIds() {
        return v;
    }

    /**
     * @return Array of adjacent (i.e. accessible) sub areas instances
     */
    public MapSubArea[] adjacentSubAreas() {
        return Arrays.stream(v).mapToObj(maps::subArea).toArray(MapSubArea[]::new);
    }

    /**
     * Check if the other sub area is adjacent to the current one
     *
     * @param other The other sub area to check
     *
     * @return true if adjacent
     */
    public boolean isAdjacent(MapSubArea other) {
        for (int sa : v) {
            if (sa == other.id) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return All maps of the current sub area
     */
    public Collection<MapPosition> maps() {
        return maps.allMapPositions().stream().filter(mapPosition -> mapPosition.subAreaId() == id).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapSubArea that = (MapSubArea) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapSubArea{" + n + " (" + id + "), area=" + area().name() + " (" + a + ")}";
    }

    void setId(int id) {
        this.id = id;
    }

    void setMaps(MapsFile maps) {
        this.maps = maps;
    }
}
