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

package fr.arakne.swflangloader;

import fr.arakne.swflangloader.lang.BaseLangFile;
import fr.arakne.swflangloader.lang.classes.ClassesFile;
import fr.arakne.swflangloader.lang.hints.HintsFile;
import fr.arakne.swflangloader.lang.lang.LangFile;
import fr.arakne.swflangloader.lang.maps.MapsFile;
import fr.arakne.swflangloader.loader.SwfFileLoader;
import fr.arakne.swflangloader.loader.TxtVersionsLoader;
import fr.arakne.swflangloader.loader.VersionsLoader;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Loader for Dofus swf lang files
 */
final public class LangLoader {
    final private URL baseUrl;
    final private String language;
    final private VersionsLoader versionsLoader;
    final private SwfFileLoader swfFileLoader;

    final private Map<String, BaseLangFile> langs = new HashMap<>();
    private MapsFile maps = null;
    private ClassesFile classes = null;
    private HintsFile hints = null;
    private LangFile lang = null;
    private Map<String, Integer> versions = null;

    public LangLoader(URL baseUrl, String language) {
        this(baseUrl, language, new TxtVersionsLoader(), new SwfFileLoader());
    }

    public LangLoader(URL baseUrl, String language, VersionsLoader versionsLoader, SwfFileLoader swfFileLoader) {
        this.baseUrl = baseUrl;
        this.language = language;
        this.versionsLoader = versionsLoader;
        this.swfFileLoader = swfFileLoader;
    }

    /**
     * Load an SWF lang File
     *
     * @param name The file name
     *
     * @return The loaded file
     *
     * @throws IOException When cannot load the file
     */
    public BaseLangFile load(String name) throws IOException, InterruptedException {
        BaseLangFile file;

        if ((file = langs.get(name)) != null) {
            return file;
        }

        file = BaseLangFile.load(url(name), swfFileLoader);
        langs.put(name, file);

        return file;
    }

    /**
     * Load maps file
     * Note: Keep the loaded instance in memory
     *
     * @return MapsFile instance
     * @throws IOException When cannot load the file
     */
    public MapsFile maps() throws IOException, InterruptedException {
        if (maps != null) {
            return maps;
        }

        return maps = new MapsFile(url("maps"), swfFileLoader);
    }

    /**
     * Load classes file
     * Note: Keep the loaded instance in memory
     *
     * @return ClassesFile instance
     * @throws IOException When cannot load the file
     */
    public ClassesFile classes() throws IOException, InterruptedException {
        if (classes != null) {
            return classes;
        }

        return classes = new ClassesFile(url("classes"), swfFileLoader);
    }

    /**
     * Load hints file
     * Note: Keep the loaded instance in memory
     *
     * @return HintsFile instance
     * @throws IOException When cannot load the file
     */
    public HintsFile hints() throws IOException, InterruptedException {
        if (hints != null) {
            return hints;
        }

        return hints = new HintsFile(url("hints"), swfFileLoader);
    }

    /**
     * Load lang file
     * Note: Keep the loaded instance in memory
     *
     * @return LangFile instance
     * @throws IOException When cannot load the file
     */
    public LangFile lang() throws IOException, InterruptedException {
        if (lang != null) {
            return lang;
        }

        return lang = new LangFile(url("lang"), swfFileLoader);
    }

    /**
     * Clear loaded files and cache
     * Permit to refresh langs versions
     *
     * @throws IOException When cannot clear cache
     */
    public void clear() throws IOException {
        versions = null;
        langs.clear();
        maps = null;
        classes = null;
        hints = null;
        lang = null;
        swfFileLoader.clear();
    }

    private int version(String file) throws IOException {
        if (versions == null) {
            versions = versionsLoader.forLanguage(baseUrl, language);
        }

        Integer version = versions.get(file);

        if (version != null) {
            return version;
        }

        throw new NoSuchElementException("The swf file " + file + " is not found");
    }

    private URL url(String file) throws IOException {
        return new URL(
            baseUrl.getProtocol(),
            baseUrl.getHost(),
            baseUrl.getPort(),
            baseUrl.getFile() + "/swf/" + file + "_" + language + "_" + version(file) + ".swf"
        );
    }
}
