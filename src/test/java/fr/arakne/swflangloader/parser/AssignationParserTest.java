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

package fr.arakne.swflangloader.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class AssignationParserTest {
    static public class ComplexType {
        public int a;
        public String b;

        public ComplexType() {
        }

        public ComplexType(int a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ComplexType that = (ComplexType) o;
            return a == that.a &&
                Objects.equals(b, that.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    private AssignationParser parser;

    @BeforeEach
    void setUp() {
        parser = new AssignationParser();
    }

    @Test
    void parseSimpleWithoutDeclaredType() {
        Assignation assignation = parser.parseLine("FOO = \"bar\";");

        assertFalse(assignation.isAssociative());
        assertFalse(assignation.isNull());
        assertEquals("FOO", assignation.variableName());
        assertEquals(new JsonPrimitive("bar"), assignation.value());

        assertEquals(new JsonPrimitive(123), parser.parseLine("FOO = 123;").value());
        assertEquals(new JsonPrimitive(12.3), parser.parseLine("FOO = 12.3;").value());
        assertEquals(new JsonPrimitive(true), parser.parseLine("FOO = true;").value());

        JsonObject expects = new JsonObject();
        expects.addProperty("foo", "bar");
        assertEquals(expects, parser.parseLine("FOO = {\"foo\":\"bar\"};").value());
    }

    @Test
    void parseShouldIgnoreCastFunction() {
        assertEquals(new JsonPrimitive(123), parser.parseLine("FOO = Number(123);").value());
        assertEquals(new JsonPrimitive(12.3), parser.parseLine("FOO = Number(12.3);").value());
        assertEquals(new JsonPrimitive(true), parser.parseLine("FOO = Boolean(true);").value());
        assertEquals(new JsonPrimitive("foo"), parser.parseLine("FOO = String(\"foo\");").value());
    }

    @Test
    void parseAssocWithoutDeclaredType() {
        Assignation assignation = parser.parseLine("FOO[123] = \"bar\";");

        assertTrue(assignation.isAssociative());
        assertFalse(assignation.isNull());
        assertEquals("FOO", assignation.variableName());
        assertEquals("123", assignation.key());
        assertEquals(new JsonPrimitive("bar"), assignation.value());

        assertEquals("bar", parser.parseLine("FOO[\"bar\"] = true;").key());
    }

    @Test
    void parseWithDeclaredType() {
        parser
            .declareSimple("FOO", String.class)
            .declareIntegerMap("OOF", String.class)
            .declareStringMap("BAR", Integer.class)
            .declareSimple("COMPLEX", ComplexType.class)
        ;

        assertEquals(new Assignation("FOO", null, "bar"), parser.parseLine("FOO = \"bar\";"));
        assertEquals(new Assignation("OOF", 123, "bar"), parser.parseLine("OOF[123] = \"bar\";"));
        assertEquals(new Assignation("BAR", "abc", 456), parser.parseLine("BAR[\"abc\"] = 456;"));
        assertEquals(new Assignation("COMPLEX", null, new ComplexType(42, "test")), parser.parseLine("COMPLEX = {a:42,b:\"test\"};"));
    }

    @Test
    void parseInvalid() {
        parser
            .declareSimple("FOO", String.class)
            .declareIntegerMap("OOF", String.class)
            .declareStringMap("BAR", Integer.class)
            .declareSimple("COMPLEX", ComplexType.class)
        ;

        assertTrue(parser.parseLine("").isNull());
        assertTrue(parser.parseLine("FOO = ").isNull());
        assertTrue(parser.parseLine("FOO = ;").isNull());
        assertTrue(parser.parseLine("= 123;").isNull());
        assertTrue(parser.parseLine("TEST = 123").isNull());
        assertTrue(parser.parseLine("OOF[\"aaa\"] = \"bbb\";").isNull());
        assertTrue(parser.parseLine("OOF = \"bbb\";").isNull());
    }
}
