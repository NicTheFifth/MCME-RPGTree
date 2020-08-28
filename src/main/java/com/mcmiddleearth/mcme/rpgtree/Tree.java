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

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Tree {
    @Getter
    @Setter
    private Long growthStage;
    @Getter
    @Setter
    private Location loc;
    @Getter
    @Setter
    private Integer id;

    public void Tree(){growthStage = 1L;}

    public void increaseGrowthStage(String Type) {
        //TODO make growtstates update trees!
        growthStage++;
        File file = new File(RPGTree.getGrowthStateDirectory()+ RPGTree.getFileSep() + Type + "_" + growthStage + ".schem");
        BlockVector3 newVec = BlockVector3.ONE;
        newVec.setComponents(loc.getX(), loc.getY(), loc.getZ());
        if(file.exists()){
        try {
            ClipboardFormats.findByFile(file).load(file).paste((World) loc.getWorld(), newVec, true, false, null);
        }catch(Exception e){
            Logger.getLogger("RPGTree").log(Level.WARNING, "Growth update failed.");
            Logger.getLogger("RPGTree").log(Level.WARNING, e.getMessage());
        }
        }else
            Logger.getLogger("RPGTree").log(Level.WARNING, "Missing growth file " + file.getPath());
    }

    public void setLoc(Location location){
        loc = location;
    }
}
