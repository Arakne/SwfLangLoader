package fr.arakne.swflangloader.lang.hints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HintsFileTest {
    static private HintsFile hints;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        hints = new HintsFile(new URL("http://arakne.fr/dofus/dofus-1-29/lang/swf/hints_fr_116.swf"));
    }

    @AfterAll
    static void afterAll() {
        hints = null;
    }

    @Test
    void filename() {
        assertEquals("hints", hints.name());
        assertEquals("fr", hints.language());
        assertEquals(116, hints.version());
    }

    @Test
    void hints() {
        assertEquals(318, hints.stream().count());

        int count = 0;

        for (Hint hint : hints) {
            assertNotNull(hint);

            assertSame(hint.category(), hints.category(hint.categoryId()));
            ++count;
        }

        assertEquals(318, count);

        AtomicInteger aCount = new AtomicInteger();
        hints.forEach(hint -> aCount.incrementAndGet());
        assertEquals(318, aCount.get());

        List<Hint> hintList = hints.stream().collect(Collectors.toList());

        assertEquals(hintList.get(5), hintList.get(5));
        assertEquals(hintList.get(5).hashCode(), hintList.get(5).hashCode());
        assertNotEquals(hintList.get(5), hintList.get(8));
        assertNotEquals(hintList.get(5).hashCode(), hintList.get(8).hashCode());

        assertEquals("Hint{Temple Crâ (90), map=540}", hintList.get(5).toString());
    }

    @Test
    void byType() {
        assertEquals(8, hints.byType(Hint.TYPE_BANK).size());

        for (Hint hint : hints.byType(Hint.TYPE_BANK)) {
            assertEquals("Banque", hint.name());
            assertEquals(Hint.TYPE_BANK, hint.type());
            assertTrue(hint.is(Hint.TYPE_BANK));
            assertFalse(hint.is(Hint.TYPE_ARENA));
            assertEquals(4, hint.categoryId());
            assertEquals("Divers", hint.category().name());
            assertEquals("Beige", hint.category().color());
        }

        assertEquals(1, hints.byType(Hint.TYPE_MOON_CANNON).size());
        for (Hint hint : hints.byType(Hint.TYPE_MOON_CANNON)) {
            assertEquals(1014, hint.mapId());
        }
    }

    @Test
    void category() {
        assertEquals(1, hints.category(1).id());
        assertEquals("Lieux de classes", hints.category(1).name());
        assertEquals("Orange", hints.category(1).color());

        assertEquals(3, hints.category(3).id());
        assertEquals("Ateliers", hints.category(3).name());
        assertEquals("Green", hints.category(3).color());

        assertSame(hints.category(1), hints.category(1));

        assertEquals(hints.category(1), hints.category(1));
        assertEquals(hints.category(1).hashCode(), hints.category(1).hashCode());
        assertNotEquals(hints.category(1), hints.category(2));
        assertNotEquals(hints.category(1).hashCode(), hints.category(2).hashCode());

        assertEquals("HintCategory{Lieux de classes (1)}", hints.category(1).toString());
    }
}
