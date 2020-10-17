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
