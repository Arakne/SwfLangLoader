package fr.arakne.swflangloader.lang.hints;

import fr.arakne.swflangloader.loader.AbstractSwfFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfVariable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * File storing map hints (i.e. point of interests like Zaap, banks...)
 * This object is iterable : you can access to hints using a for loop
 */
final public class HintsFile extends AbstractSwfFile implements Iterable<Hint> {
    final static private MapperHydrator<HintsFile> HYDRATOR = MapperHydrator.parseAnnotations(HintsFile.class);

    @SwfVariable("HIC")
    final private Map<Integer, HintCategory> categories = new HashMap<>();

    @SwfVariable("HI")
    private Hint[] hints;

    public HintsFile(URL file, SwfFileLoader loader) throws IOException, InterruptedException {
        loader.load(file, this, HYDRATOR);
        init();
    }

    public HintsFile(URL file) throws IOException, InterruptedException {
        this(file, new SwfFileLoader());
    }

    public HintsFile(File file) throws IOException, InterruptedException {
        this(file.toURI().toURL(), new SwfFileLoader());
    }

    /**
     * Get a hint category by its id
     *
     * @param id The color id
     *
     * @return The color or null is not found
     */
    public HintCategory category(int id) {
        return categories.get(id);
    }

    /**
     * @return Stream of all hints
     */
    public Stream<Hint> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Get all hints of the given type
     *
     * @param type The hint type id. One of the Hint.TYPE_* constant
     *
     * @return All matching hints
     */
    public Collection<Hint> byType(int type) {
        return stream().filter(hint -> hint.is(type)).collect(Collectors.toList());
    }

    @Override
    public Iterator<Hint> iterator() {
        return stream().iterator();
    }

    @Override
    public Spliterator<Hint> spliterator() {
        return Arrays.spliterator(hints);
    }

    @Override
    public void forEach(Consumer<? super Hint> action) {
        for (Hint hint : hints) {
            action.accept(hint);
        }
    }

    private void init() {
        categories.forEach((id, color) -> color.setId(id));
        forEach(hint -> hint.setHints(this));
    }
}
