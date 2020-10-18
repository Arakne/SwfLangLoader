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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Load version from versions_xx.txt file
 */
final public class TxtVersionsLoader implements VersionsLoader {
    final static private String VERSION_FILE_PREFIX = "versions_";
    final static private String VERSION_FILE_EXTENSION = ".txt";

    @Override
    public Map<String, Integer> forLanguage(URL baseUrl, String language) throws IOException {
        final URL url = versionFile(baseUrl, language);

        try (InputStream stream = new BufferedInputStream(url.openStream())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            return parseVersions(reader.readLine(), language);
        }
    }

    private URL versionFile(URL baseUrl, String language) throws MalformedURLException {
        return new URL(
            baseUrl.getProtocol(),
            baseUrl.getHost(),
            baseUrl.getPort(),
            baseUrl.getFile() + "/" + VERSION_FILE_PREFIX + language + VERSION_FILE_EXTENSION
        );
    }

    private Map<String, Integer> parseVersions(String content, String language) {
        if (!content.startsWith("&f=")) {
            throw new IllegalArgumentException("Invalid versions file");
        }

        Map<String, Integer> versions = new HashMap<>();

        for (int pos = 3, nextPos; (nextPos = content.indexOf('|', pos)) != -1 ; pos = nextPos + 1) {
            final String[] parts = content.substring(pos, nextPos).split(",", 3);

            if (parts.length != 3 || !parts[1].equals(language)) {
                continue;
            }

            versions.put(parts[0], Integer.parseInt(parts[2]));
        }

        return versions;
    }
}
