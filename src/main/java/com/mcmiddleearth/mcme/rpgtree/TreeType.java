/*
 * This file is part of MCME-pvp.
 *
 * MCME-pvp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MCME-pvp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MCME-pvp.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package com.mcmiddleearth.mcme.rpgtree;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class TreeType {
    @Getter
    private Set<Tree> trees = new HashSet<>();
    @Getter
    @Setter
    private Integer growthMinutes;
    @Getter
    @Setter
    private Integer maxGrowthStage;
    @Getter
    @Setter
    private String type;
}
