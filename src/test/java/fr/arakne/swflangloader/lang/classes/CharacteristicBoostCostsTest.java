package fr.arakne.swflangloader.lang.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacteristicBoostCostsTest {
    private CharacteristicBoostCosts boostCosts;

    @BeforeEach
    void setUp() {
        boostCosts = new CharacteristicBoostCosts(new int[][] {new int[] {0,1},new int[] {20,2},new int[] {40,3},new int[] {60,4},new int[] {80,5}});
    }

    @Test
    void at() {
        assertEquals(1, boostCosts.at(0).cost());
        assertEquals(1, boostCosts.at(0).boost());

        assertEquals(1, boostCosts.at(15).cost());
        assertEquals(1, boostCosts.at(15).boost());

        assertEquals(2, boostCosts.at(30).cost());
        assertEquals(1, boostCosts.at(30).boost());

        assertEquals(5, boostCosts.at(145).cost());
        assertEquals(1, boostCosts.at(145).boost());

        assertThrows(IllegalArgumentException.class, () -> boostCosts.at(-5));

        boostCosts = new CharacteristicBoostCosts(new int[][] {new int[] {0, 1, 2}});
        assertEquals(1, boostCosts.at(145).cost());
        assertEquals(2, boostCosts.at(145).boost());
    }

    @Test
    void total() {
        assertEquals(0, boostCosts.total(0));
        assertEquals(10, boostCosts.total(10));
        assertEquals(40, boostCosts.total(30));
        assertEquals(525, boostCosts.total(145));

        boostCosts = new CharacteristicBoostCosts(new int[][] {new int[] {0, 1, 2}});
        assertEquals(75, boostCosts.total(150));
    }
}
