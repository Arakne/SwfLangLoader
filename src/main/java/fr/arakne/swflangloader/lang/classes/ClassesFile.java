package fr.arakne.swflangloader.lang.classes;

import fr.arakne.swflangloader.loader.AbstractSwfFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import fr.arakne.swflangloader.parser.mapper.SwfVariable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Store classes data
 */
final public class ClassesFile extends AbstractSwfFile {
    final static private MapperHydrator<ClassesFile> HYDRATOR = MapperHydrator.parseAnnotations(ClassesFile.class);

    @SwfVariable("G")
    final private Map<Integer, DofusClass> classes = new HashMap<>();

    public ClassesFile(URL file, SwfFileLoader loader) throws IOException, InterruptedException {
        loader.load(file, this, HYDRATOR);
        init();
    }

    public ClassesFile(URL file) throws IOException, InterruptedException {
        this(file, new SwfFileLoader());
    }

    public ClassesFile(File file) throws IOException, InterruptedException {
        this(file.toURI().toURL(), new SwfFileLoader());
    }

    /**
     * @return All available classes
     */
    public Collection<DofusClass> all() {
        return classes.values();
    }

    /**
     * Get a class by its id
     *
     * @param id The class id
     *
     * @return The class data
     */
    public DofusClass get(int id) {
        return classes.get(id);
    }

    private void init() {
        classes.forEach((id, dofusClass) -> dofusClass.setId(id));
    }
}
