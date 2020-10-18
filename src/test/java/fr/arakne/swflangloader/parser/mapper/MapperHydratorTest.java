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

package fr.arakne.swflangloader.parser.mapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapperHydratorTest {
    static public class Embedded {
        public String key;
        public String value;
    }

    static public class Target {
        public String foo;
        public Map<Integer, String> bar = new HashMap<>();
        public Map<String, Embedded> embeddeds = new HashMap<>();
        public Map<String, Object> values;
    }

    private MapperHydrator<Target> hydrator;

    @BeforeEach
    void setUp() {
        hydrator = new MapperHydrator<>();
        hydrator.declareSimpleProperty("FOO", String.class, new PropertyHydrator<Target, String>() {
            @Override
            public String get(Target source) {
                return source.foo;
            }

            @Override
            public void set(Target target, String value) {
                target.foo = value;
            }
        });
        hydrator.declareMapProperty("BAR", Integer.class, String.class, new PropertyHydrator<Target, Map<Integer, String>>() {
            @Override
            public Map<Integer, String> get(Target source) {
                return source.bar;
            }

            @Override
            public void set(Target target, Map<Integer, String> value) {
                target.bar = value;
            }
        });
        hydrator.declareMapProperty("EMB", String.class, Embedded.class, new PropertyHydrator<Target, Map<String, Embedded>>() {
            @Override
            public Map<String, Embedded> get(Target source) {
                return source.embeddeds;
            }

            @Override
            public void set(Target target, Map<String, Embedded> value) {
                target.embeddeds = value;
            }
        });
        hydrator.setDefaultHydrator(new PropertyHydrator<Target, Map<String, Object>>() {
            @Override
            public Map<String, Object> get(Target source) {
                return source.values;
            }

            @Override
            public void set(Target target, Map<String, Object> value) {
                target.values = value;
            }
        });
    }

    @Test
    void hydrate() {
        Target target = new Target();

        hydrator.hydrate(target, "FOO = 123;");
        assertEquals("123", target.foo);

        hydrator.hydrate(target, "BAR[123] = \"a\";");
        hydrator.hydrate(target, "BAR[456] = \"b\";");
        hydrator.hydrate(target, "BAR[789] = \"c\";");

        assertEquals(3, target.bar.size());
        assertEquals("a", target.bar.get(123));
        assertEquals("b", target.bar.get(456));
        assertEquals("c", target.bar.get(789));

        hydrator.hydrate(target, "EMB[\"a\"] = {key: \"k\", value: \"v\"};");
        hydrator.hydrate(target, "EMB[\"b\"] = {key: \"l\", value: \"w\"};");

        assertEquals(2, target.embeddeds.size());
        assertEquals("k", target.embeddeds.get("a").key);
        assertEquals("v", target.embeddeds.get("a").value);
        assertEquals("l", target.embeddeds.get("b").key);
        assertEquals("w", target.embeddeds.get("b").value);

        hydrator.hydrate(target, "OTHER = 123;");
        assertEquals(1, target.values.size());
        assertEquals(new JsonPrimitive(123), target.values.get("OTHER"));

        hydrator.hydrate(target, "MAP[1] = {a:3};");
        hydrator.hydrate(target, "MAP[2] = {a:8};");
        assertEquals(2, target.values.size());
        assertTrue(target.values.get("MAP") instanceof Map);
        assertEquals(2, ((Map<?, ?>) target.values.get("MAP")).size());
        JsonObject jo = new JsonObject();
        jo.addProperty("a", 8);
        assertEquals(jo, ((Map<?, ?>) target.values.get("MAP")).get("2"));

        hydrator.hydrate(target, "invalid");
        hydrator.hydrate(target, "EMB[\"c\"] = 123;");
        assertEquals(2, target.embeddeds.size());
        assertFalse(target.embeddeds.containsKey("c"));
    }

    static public class AnnotatedTarget {
        @SwfVariable
        private String foo;

        @SwfVariable("MAPPED")
        private int withMapping;

        @SwfVariable
        final private Map<Integer, String> kv = new HashMap<>();

        @SwfDefault
        final private Map<String, JsonElement> values = new HashMap<>();
    }

    @Test
    void withAnnotations() {
        MapperHydrator<AnnotatedTarget> hydrator = MapperHydrator.parseAnnotations(AnnotatedTarget.class);

        AnnotatedTarget target = new AnnotatedTarget();

        hydrator.hydrate(target, "foo = \"bar\";");
        assertEquals("bar", target.foo);

        hydrator.hydrate(target, "MAPPED = 123;");
        assertEquals(123, target.withMapping);

        hydrator.hydrate(target, "kv[1] = \"a\";");
        hydrator.hydrate(target, "kv[2] = \"b\";");
        assertEquals(2, target.kv.size());
        assertEquals("a", target.kv.get(1));
        assertEquals("b", target.kv.get(2));

        hydrator.hydrate(target, "undefined = 123;");
        hydrator.hydrate(target, "other = true;");
        assertEquals(2, target.values.size());
        assertEquals(new JsonPrimitive(123), target.values.get("undefined"));
        assertEquals(new JsonPrimitive(true), target.values.get("other"));
    }
}
