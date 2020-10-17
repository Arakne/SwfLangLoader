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
