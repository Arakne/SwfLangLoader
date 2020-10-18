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

import java.util.Objects;

/**
 * Positions and extra information for a single game map
 *
 * Note: The coordinates are not unique
 */
final public class MapPosition {
    private int id;
    private MapsFile maps;

    /* final */ private int x;
    /* final */ private int y;
    /* final */ private int sa;
    /* final */ private int ep;

    /**
     * @return The map id. Unique on the maps file
     */
    public int id() {
        return id;
    }

    /**
     * The X coordinate
     * Lower value for north, and higher for south
     *
     * @return The coordinate value
     */
    public int x() {
        return x;
    }

    /**
     * The Y coordinate
     * Lower value for west, higher value for east
     *
     * @return The coordinate value
     */
    public int y() {
        return y;
    }

    /**
     * @return The sub area id
     */
    public int subAreaId() {
        return sa;
    }

    /**
     * @return The sub area instance
     */
    public MapSubArea subArea() {
        return maps.subArea(sa);
    }

    /**
     * @return The ep value
     */
    public int ep() {
        return ep;
    }

    /**
     * Compute manhattan distance between the two maps positions
     *
     * @param other The other map position
     *
     * @return The distance
     */
    public int distance(MapPosition other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapPosition position = (MapPosition) o;
        return id == position.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapPosition{" + id +
            ", x=" + x +
            ", y=" + y +
            ", sa=" + subArea().name() + " (" + sa + ")" +
            '}';
    }

    void setId(int id) {
        this.id = id;
    }

    void setMaps(MapsFile maps) {
        this.maps = maps;
    }
}
