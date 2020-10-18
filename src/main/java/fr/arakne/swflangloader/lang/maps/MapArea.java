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
 * Dofus map area
 */
final public class MapArea {
    private int id;
    private MapsFile maps;

    private String n;
    private int sua;

    /**
     * @return The area id
     */
    public int id() {
        return id;
    }

    /**
     * @return The area name
     */
    public String name() {
        return n;
    }

    /**
     * @return Super area id. Unique on the maps file
     */
    public int superAreaId() {
        return sua;
    }

    /**
     * @return Super area name
     */
    public String superArea() {
        return maps.superArea(sua);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapArea area = (MapArea) o;
        return id == area.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapArea{" + n + " (" + id + ")}";
    }

    void setId(int id) {
        this.id = id;
    }

    void setMaps(MapsFile maps) {
        this.maps = maps;
    }
}
