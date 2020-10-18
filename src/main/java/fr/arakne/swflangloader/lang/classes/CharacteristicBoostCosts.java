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

package fr.arakne.swflangloader.lang.classes;

/**
 * Handle boosts intervals and costs
 */
final public class CharacteristicBoostCosts {
    final static public class Cost {
        final private int cost;
        final private int boost;

        public Cost(int cost, int boost) {
            this.cost = cost;
            this.boost = boost;
        }

        /**
         * @return The characteristic boost points cost
         */
        public int cost() {
            return cost;
        }

        /**
         * @return The boosted characteristics value
         */
        public int boost() {
            return boost;
        }
    }

    final private int[][] costs;

    public CharacteristicBoostCosts(int[][] costs) {
        this.costs = costs;
    }

    /**
     * Get the characteristic boost cost for the current characteristic value
     *
     * @param current The current characteristic value
     *
     * @return The cost
     */
    public Cost at(int current) {
        if (current < 0) {
            throw new IllegalArgumentException("Current characteristic value cannot be less than zero");
        }

        for (int i = costs.length - 1; i >= 0; --i) {
            if (costs[i][0] <= current) {
                return new Cost(costs[i][1], costs[i].length == 3 ? costs[i][2] : 1);
            }
        }

        throw new IllegalArgumentException("Cannot found any valid boost cost");
    }

    /**
     * Get the total points cost needed to reach the current value
     *
     * @param current The current value
     *
     * @return The spent points
     */
    public int total(int current) {
        int total = 0;

        for (int i = costs.length - 1; i >= 0; --i) {
            int[] cost = costs[i];

            if (cost[0] > current) {
                continue;
            }

            int value = current - cost[0];

            if (cost.length == 3) {
                value /= cost[2];
            }

            total += value * cost[1];
            current = cost[0];
        }

        return total;
    }

    /**
     * @return Raw costs
     */
    public int[][] costs() {
        return costs;
    }
}
