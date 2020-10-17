package fr.arakne.swflangloader.lang.hints;

import java.util.Objects;

/**
 * Hint category
 */
final public class HintCategory {
    private int id;

    /* final */ private String n;
    /* final */ private String c;

    /**
     * @return The category name
     */
    public String name() {
        return n;
    }

    /**
     * @return The category color
     */
    public String color() {
        return c;
    }

    /**
     * @return The category id
     */
    public int id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HintCategory that = (HintCategory) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HintCategory{" + n + " (" + id + ")}";
    }

    void setId(int id) {
        this.id = id;
    }
}
