package fr.arakne.swflangloader.lang.lang;

import fr.arakne.swflangloader.lang.BaseLangFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * The lang_xx_xxx.swf file
 * Contains translations and global configurations
 */
final public class LangFile extends BaseLangFile {
    final static private MapperHydrator<LangFile> HYDRATOR = MapperHydrator.parseAnnotations(LangFile.class);

    public LangFile(URL file, SwfFileLoader loader) throws IOException, InterruptedException {
        loader.load(file, this, HYDRATOR);
    }

    public LangFile(URL file) throws IOException, InterruptedException {
        this(file, new SwfFileLoader());
    }

    public LangFile(File file) throws IOException, InterruptedException {
        this(file.toURI().toURL(), new SwfFileLoader());
    }

    /**
     * Get an error message (packet Im1)
     *
     * @param id The error message id
     *
     * @return The message
     */
    public String error(int id) {
        return string("ERROR_" + id);
    }

    /**
     * Get an info message (packet Im0)
     *
     * @param id The info message id
     *
     * @return The message
     */
    public String info(int id) {
        return string("INFOS_" + id);
    }

    /**
     * Get an pvp message (packet Im0)
     *
     * @param id The pvp message id
     *
     * @return The message
     */
    public String pvp(int id) {
        return string("PVP_" + id);
    }
}
