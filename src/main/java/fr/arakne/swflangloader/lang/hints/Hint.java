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

package fr.arakne.swflangloader.lang.hints;

import java.util.Objects;

/**
 * A map hint
 */
final public class Hint {
    final static public int TYPE_PLACE_FECA = 10;
    final static public int TYPE_PLACE_OSAMODA = 20;
    final static public int TYPE_PLACE_ENUTROF = 30;
    final static public int TYPE_PLACE_SRAM = 40;
    final static public int TYPE_PLACE_XELOR = 50;
    final static public int TYPE_PLACE_ECAFLIP = 60;
    final static public int TYPE_PLACE_ENIRIPSA = 70;
    final static public int TYPE_PLACE_IOP = 80;
    final static public int TYPE_PLACE_CRA = 90;
    final static public int TYPE_PLACE_SADIDA = 100;
    final static public int TYPE_PLACE_SACRIEUR = 110;
    final static public int TYPE_PLACE_PANDAWA = 120;

    final static public int TYPE_SALE_HOTEL_ALCHEMIST = 201;
    final static public int TYPE_SALE_HOTEL_JEWELER = 202;
    final static public int TYPE_SALE_HOTEL_BUTCHER = 203;
    final static public int TYPE_SALE_HOTEL_BAKER = 204;
    final static public int TYPE_SALE_HOTEL_LUMBERJACK = 205;
    final static public int TYPE_SALE_HOTEL_HUNTER = 206;
    final static public int TYPE_SALE_HOTEL_SHOEMAKER = 207;
    final static public int TYPE_SALE_HOTEL_BLACKSMITH = 208;
    final static public int TYPE_SALE_HOTEL_MINER = 209;
    final static public int TYPE_SALE_HOTEL_FARMER = 210;
    final static public int TYPE_SALE_HOTEL_SINNER = 211;
    final static public int TYPE_SALE_HOTEL_FISHMONGER = 212;
    final static public int TYPE_SALE_HOTEL_RESOURCE = 213;
    final static public int TYPE_SALE_HOTEL_SCULPTOR = 214;
    final static public int TYPE_SALE_HOTEL_TAILOR = 215;
    final static public int TYPE_SALE_HOTEL_SHIELD = 216;
    final static public int TYPE_SALE_HOTEL_SOUL = 217;
    final static public int TYPE_SALE_HOTEL_PET = 218;
    final static public int TYPE_SALE_HOTEL_PARCHMENT = 219;
    final static public int TYPE_SALE_HOTEL_RUNE = 220;
    final static public int TYPE_SALE_HOTEL_DOCUMENT = 221;
    final static public int TYPE_SALE_HOTEL_HANDYMAN = 222;
    final static public int TYPE_SALE_HOTEL_FIREWORK = 223;

    final static public int TYPE_WORKSHOP_ALCHEMIST = 301;
    final static public int TYPE_WORKSHOP_JEWELER = 302;
    final static public int TYPE_WORKSHOP_BUTCHER = 303;
    final static public int TYPE_WORKSHOP_BAKER = 304;
    final static public int TYPE_WORKSHOP_LUMBERJACK = 305;
    final static public int TYPE_WORKSHOP_HUNTER = 306;
    final static public int TYPE_WORKSHOP_SHOEMAKER = 307;
    final static public int TYPE_WORKSHOP_SMITHMAGIC = 308;
    final static public int TYPE_WORKSHOP_BLACKSMITH = 309;
    final static public int TYPE_WORKSHOP_MINER = 310;
    final static public int TYPE_WORKSHOP_FARMER = 311;
    final static public int TYPE_WORKSHOP_SINNER = 312;
    final static public int TYPE_WORKSHOP_SCULPTOR = 314;
    final static public int TYPE_WORKSHOP_TAILOR = 315;
    final static public int TYPE_WORKSHOP_SHIELD = 316;
    final static public int TYPE_WORKSHOP_HANDYMAN = 317;
    final static public int TYPE_WORKSHOP_FIREWORK = 318;

    final static public int TYPE_BANK = 401;
    final static public int TYPE_MOON_CANNON = 402;
    final static public int TYPE_CHURCH = 403;
    final static public int TYPE_KENNEL = 404;
    final static public int TYPE_GUILD_TEMPLE = 405;
    final static public int TYPE_KANOJEDO = 406;
    final static public int TYPE_MILITIA = 407;
    final static public int TYPE_TAVERN = 408;
    final static public int TYPE_ZAAP = 410;
    final static public int TYPE_ARENA = 411;
    final static public int TYPE_AIR_TRANSPORT = 412;
    final static public int TYPE_BOAT = 413;
    final static public int TYPE_DOJO = 414;
    final static public int TYPE_MARKET_PLACE = 415;
    final static public int TYPE_TOWER = 416;
    final static public int TYPE_LIBRARY = 417;
    final static public int TYPE_JOB_HOTEL = 418;
    final static public int TYPE_PUBLIC_PADDOCK = 419;
    final static public int TYPE_DUNGEON = 422;
    final static public int TYPE_EMOTE_STONE = 423;

    private HintsFile hints;

    /* final */ private String n;
    /* final */ private int c;
    /* final */ private int g;
    /* final */ private int m;

    /**
     * @return The hint name
     */
    public String name() {
        return n;
    }

    /**
     * @return The hint color id
     * @see HintCategory#id()
     */
    public int categoryId() {
        return c;
    }

    /**
     * @return The hint category instance
     */
    public HintCategory category() {
        return hints.category(c);
    }

    /**
     * @return The map id
     */
    public int mapId() {
        return m;
    }

    /**
     * Get the hint type
     * The type is one of the Hint.TYPE_* constant
     *
     * @return The type id
     * @see Hint#is(int) To check the type
     */
    public int type() {
        return g;
    }

    /**
     * Check if the hint is of the given type
     *
     * @param type The type id. Can be one of the Hint.TYPE_* constant
     *
     * @return true if the type match
     * @see Hint#type() To get the type
     */
    public boolean is(int type) {
        return g == type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hint hint = (Hint) o;
        return g == hint.g && m == hint.m;
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, m);
    }

    @Override
    public String toString() {
        return "Hint{" + n + " (" + g + "), map=" + m + "}";
    }

    void setHints(HintsFile hints) {
        this.hints = hints;
    }
}
