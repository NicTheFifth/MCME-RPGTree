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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Donovan <dallen@dallen.xyz>
 */
public class EventLocation {
    
    @Getter @Setter private int X;

    @Getter @Setter private int Y;

    @Getter @Setter private int Z;
    
    @Getter @Setter private String World;
    
    public EventLocation(){}
    
    public EventLocation(Location l){
        this.X = l.getBlockX();
        this.Y = l.getBlockY();
        this.Z = l.getBlockZ();
        this.World = l.getWorld().getName();
    }
    
    public Location toBukkitLoc(){
        return new Location(Bukkit.getWorld(World), X, Y, Z);
    }
}
