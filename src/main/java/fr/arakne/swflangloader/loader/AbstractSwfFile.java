package fr.arakne.swflangloader.loader;

/**
 * Base Swf file type, used by loader to inject name language and version
 */
abstract public class AbstractSwfFile {
    private String name;
    private String language;
    private int version;

    /**
     * @return The name of the swf file (Following filename [name]_[language]_[version].swf)
     */
    final public String name() {
        return name;
    }

    /**
     * @return The language of the swf file (Following filename [name]_[language]_[version].swf)
     */
    final public String language() {
        return language;
    }

    /**
     * @return The version of the swf file (Following filename [name]_[language]_[version].swf)
     */
    final public int version() {
        return version;
    }

    void setName(String name) {
        this.name = name;
    }

    void setLanguage(String language) {
        this.language = language;
    }

    void setVersion(int version) {
        this.version = version;
    }
}
