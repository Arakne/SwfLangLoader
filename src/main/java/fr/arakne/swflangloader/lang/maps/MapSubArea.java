package fr.arakne.swflangloader.lang.maps;

import java.util.Arrays;
import java.util.Objects;

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
