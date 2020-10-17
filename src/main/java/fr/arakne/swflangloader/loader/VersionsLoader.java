package fr.arakne.swflangloader.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Loader for SWF files version
 */
public interface VersionsLoader {
    /**
     * Load versions for the given language
     *
     * @param baseUrl The base lang URL
     * @param language The language to load
     *
     * @return A map of versions, indexed by lang file name
     *
     * @throws IOException When cannot load the versions file
     */
    public Map<String, Integer> forLanguage(URL baseUrl, String language) throws IOException;
}
