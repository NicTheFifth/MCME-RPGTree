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

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;

public class TreeType {
    @Getter
    private Set<Tree> trees = new HashSet<>();
    @Getter
    @Setter
    private Long growthMinutes;
    @Getter
    @Setter
    private Long maxGrowthStage;
    @Getter
    @Setter
    private String type;

    public void TreeType(){}

    public void addTree(Player p){
        Tree newTree = new Tree();
        newTree.setLoc(p.getLocation());
        newTree.setGrowthStage(1L);
        newTree.setId(trees.size());
        trees.add(newTree);
    }

    public void removeTree(int toRemove){
    trees.stream().filter(tree -> tree.getId()>=toRemove).forEach(tree -> {
        if(tree.getId() == toRemove)
            trees.remove(tree);
        else
            tree.setId(tree.getId() - 1);
    });
    }

    public boolean hasAllStates(){
        File dir = RPGTree.getGrowthStateDirectory();
        if(growthMinutes == null)
            return true;
        for(int m=0; m<growthMinutes; m++){
            File check = new File(dir+ RPGTree.getFileSep() + type + "_" + m);
            if(!check.exists()){
                return false;
            }
        }
        return true;
    }
}
