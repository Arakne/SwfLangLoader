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
