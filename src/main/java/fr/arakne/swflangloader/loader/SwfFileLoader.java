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

package fr.arakne.swflangloader.loader;

import com.jpexs.decompiler.flash.AbortRetryIgnoreHandler;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.exporters.settings.ScriptExportSettings;
import fr.arakne.swflangloader.parser.mapper.MapperHydrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Load the SWF file to extract AS3 sources
 */
final public class SwfFileLoader {
    private final static Logger LOGGER = LoggerFactory.getLogger(SwfFileLoader.class);

    final private Path tempDir;
    final private boolean cache;

    public SwfFileLoader() {
        this(Paths.get("./tmp"), true);
    }

    /**
     * @param tempDir The temporary directory used for extract action script files
     * @param cache Enable cache (i.e. keep tmp files)
     */
    public SwfFileLoader(Path tempDir, boolean cache) {
        this.tempDir = tempDir;
        this.cache = cache;
    }

    /**
     * Load the SWF file and hydrate the target structure
     *
     * @param file SWF file URL
     * @param target The target structure
     * @param hydrator Hydrator to use
     *
     * @param <T> The structure type
     *
     * @throws IOException When error occurs during loading the SWF file
     * @throws InterruptedException When the loading is cancelled
     */
    public synchronized <T extends AbstractSwfFile> void load(URL file, T target, MapperHydrator<T> hydrator) throws IOException, InterruptedException {
        LOGGER.debug("[SWF] Loading {} to {}", file, target.getClass().getSimpleName());

        final String filename = extractFilename(file);
        parseFilename(target, filename);

        Path outdir = tempDir.resolve(filename);
        Files.createDirectories(outdir);

        try {
            List<File> sources = loadFilesFromCache(outdir);

            if (sources.isEmpty()) {
                LOGGER.debug("[SWF] {} is not cached. Load from SWF", file);
                sources = loadFilesFromSwf(file, outdir);
            }

            for (File source : sources) {
                parseFile(source, target, hydrator);
            }
        } finally {
            if (!cache) {
                clearTemp(tempDir);
            }
        }
    }

    /**
     * Clear the cache
     *
     * @throws IOException When removing cache files failed
     */
    public void clear() throws IOException {
        clearTemp(tempDir);
    }

    private String extractFilename(URL file) {
        final String path = file.getFile();
        final int filenamePos = path.lastIndexOf('/');

        return path.substring(filenamePos + 1, path.length() - 4);
    }

    private void parseFilename(AbstractSwfFile target, String filename) {
        final String[] parts = filename.split("_", 3);

        if (parts.length != 3) {
            return;
        }

        target.setName(parts[0]);
        target.setLanguage(parts[1]);

        try {
            target.setVersion(Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            // Ignore
        }
    }

    private List<File> loadFilesFromSwf(URL url, Path outdir) throws IOException, InterruptedException {
        try (InputStream stream = url.openStream()) {
            SWF swf = new SWF(stream, false);

            return swf.exportActionScript(
                new AbortRetryIgnoreHandler() {
                    @Override
                    public int handle(Throwable throwable) {
                        return AbortRetryIgnoreHandler.ABORT;
                    }

                    @Override
                    public AbortRetryIgnoreHandler getNewInstance() {
                        return this;
                    }
                },
                outdir.toAbsolutePath().toString(),
                new ScriptExportSettings(ScriptExportMode.AS, false, true),
                false,
                null
            );
        }
    }

    private List<File> loadFilesFromCache(Path cacheDir) throws IOException {
        if (!cache) {
            return Collections.emptyList();
        }

        try (final Stream<Path> files = Files.walk(cacheDir)) {
            return files
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".as"))
                .map(Path::toFile)
                .collect(Collectors.toList())
            ;
        }
    }

    /**
     * Parse file lines
     */
    private <T> void parseFile(File file, T target, MapperHydrator<T> hydrator) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                try {
                    hydrator.hydrate(target, line);
                } catch (Exception e) {
                    LOGGER.error("[SWF] Error while parsing line: {}", line, e);
                    throw e;
                }
            }
        }
    }

    /**
     * Recursive clear cache directory
     */
    private void clearTemp(Path tempdir) throws IOException {
        if (!Files.exists(tempdir)) {
            return;
        }

        Files.walkFileTree(tempdir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);

                return FileVisitResult.CONTINUE;
            }
        });
    }
}
