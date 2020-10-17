package fr.arakne.swflangloader.lang.maps;

import fr.arakne.swflangloader.loader.AbstractSwfFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfVariable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The maps lang file, containing map positions, subareas, areas, and super areas
 *
 * Usage:
 * <pre>{@code
 * MapsFile maps = new MapsFile(new URL("http://my-lang.example.com/dofus/lang/swf/maps_fr_366.swf"));
 *
 * MapPosition pos = maps.position(1547);
 *
 * pos.x(); // ...
 * pos.subArea(); // ...
 * }</pre>
 */
final public class MapsFile extends AbstractSwfFile {
    final static private MapperHydrator<MapsFile> HYDRATOR = MapperHydrator.parseAnnotations(MapsFile.class);

    final static private class Position {
        final private int x;
        final private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @SwfVariable("MA.m")
    final private Map<Integer, MapPosition> mapPosById = new HashMap<>();
    private Map<Position, List<MapPosition>> mapPosByPosition = null;

    @SwfVariable("MA.sua")
    final private Map<Integer, String> superAreas = new HashMap<>();

    @SwfVariable("MA.a")
    final private Map<Integer, MapArea> areasById = new HashMap<>();

    @SwfVariable("MA.sa")
    final private Map<Integer, MapSubArea> subAreasById = new HashMap<>();

    public MapsFile(URL file, SwfFileLoader loader) throws IOException, InterruptedException {
        loader.load(file, this, HYDRATOR);
        init();
    }

    public MapsFile(URL file) throws IOException, InterruptedException {
        this(file, new SwfFileLoader());
    }

    public MapsFile(File file) throws IOException, InterruptedException {
        this(file.toURI().toURL(), new SwfFileLoader());
    }

    /**
     * Get a simple map position by its id
     *
     * @param id The map id
     *
     * @return The map position, or null is not found
     */
    public MapPosition position(int id) {
        return mapPosById.get(id);
    }

    /**
     * @return All available map positions
     */
    public Collection<MapPosition> allMapPositions() {
        return mapPosById.values();
    }

    /**
     * Get available maps positions at the given coordinates
     *
     * @param x X coordinate
     * @param y Y coordinate
     *
     * @return The matching positions. If not found, an empty collection is returned
     */
    public Collection<MapPosition> positions(int x, int y) {
        final Position position = new Position(x, y);

        if (mapPosByPosition == null) {
            mapPosByPosition = mapPosById.values().stream().collect(Collectors.groupingBy(pos -> new Position(pos.x(), pos.y())));
        }

        final List<MapPosition> positions = mapPosByPosition.get(position);

        if (positions == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(positions);
    }

    /**
     * Get a subarea by its id
     *
     * @param id The subarea id
     *
     * @return The subarea, or null if not found
     */
    public MapSubArea subArea(int id) {
        return subAreasById.get(id);
    }

    /**
     * @return All available subareas
     */
    public Collection<MapSubArea> allSubAreas() {
        return subAreasById.values();
    }

    /**
     * Get an area by its id
     *
     * @param id The area id
     *
     * @return The area, or null if not found
     */
    public MapArea area(int id) {
        return areasById.get(id);
    }

    /**
     * @return All available areas
     */
    public Collection<MapArea> allAreas() {
        return areasById.values();
    }

    /**
     * Get a super area name by its id
     *
     * @param id The super area id
     *
     * @return The name, or null if not found
     */
    public String superArea(int id) {
        return superAreas.get(id);
    }

    private void init() {
        mapPosById.forEach((id, mapPosition) -> {
            mapPosition.setId(id);
            mapPosition.setMaps(this);
        });

        subAreasById.forEach((id, subArea) -> {
            subArea.setId(id);
            subArea.setMaps(this);
        });

        areasById.forEach((id, area) -> {
            area.setId(id);
            area.setMaps(this);
        });
    }
}
