package fr.arakne.swflangloader.lang.classes;

import java.util.Objects;

/**
 * Dofus class data
 */
final public class DofusClass {
    private int id;

    /* final */ private String sn;
    /* final */ private String ln;
    /* final */ private int ep;
    /* final */ private String d;
    /* final */ private int[] s;
    /* final */ private Object[] cc;
    /* final */ private int[][] b10;
    /* final */ private int[][] b11;
    /* final */ private int[][] b12;
    /* final */ private int[][] b13;
    /* final */ private int[][] b14;
    /* final */ private int[][] b15;

    /**
     * @return The short (i.e. single word) class name
     */
    public String shortName() {
        return sn;
    }

    /**
     * @return The long class name
     */
    public String longName() {
        return ln;
    }

    /**
     * @return The apparition episode
     */
    public int ep() {
        return ep;
    }

    /**
     * @return The class description
     */
    public String description() {
        return d;
    }

    /**
     * @return List of class spells ids
     */
    public int[] spellIds() {
        return s;
    }

    /**
     * @return Raw close combat data
     */
    public Object[] closeCombatData() {
        return cc;
    }

    /**
     * @return The class id
     */
    public int id() {
        return id;
    }

    /**
     * @return Strength boost costs
     */
    public CharacteristicBoostCosts strengthBoost() {
        return new CharacteristicBoostCosts(b10);
    }

    /**
     * @return Vitality boost costs
     */
    public CharacteristicBoostCosts vitalityBoost() {
        return new CharacteristicBoostCosts(b11);
    }

    /**
     * @return Wisdom boost costs
     */
    public CharacteristicBoostCosts wisdomBoost() {
        return new CharacteristicBoostCosts(b12);
    }

    /**
     * @return Luck boost costs
     */
    public CharacteristicBoostCosts luckBoost() {
        return new CharacteristicBoostCosts(b13);
    }

    /**
     * @return Agility boost costs
     */
    public CharacteristicBoostCosts agilityBoost() {
        return new CharacteristicBoostCosts(b14);
    }

    /**
     * @return Intelligence boost costs
     */
    public CharacteristicBoostCosts intelligenceBoost() {
        return new CharacteristicBoostCosts(b15);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DofusClass that = (DofusClass) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DofusClass{name=" + sn + ", id=" + id + "}";
    }

    void setId(int id) {
        this.id = id;
    }
}
