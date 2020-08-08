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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RPGTree extends JavaPlugin {

    @Getter
    private static RPGTree plugin;
    public Set<TreeType> treeTypes;
    @Getter
    private static File pluginDirectory;
    @Getter
    private static String FileSep = System.getProperty("file.separator");

    @Override
    public void onEnable() {
        Logger.getLogger("RPGTree").log(Level.INFO, "RPGTree loaded correctly");
        plugin = this;

        pluginDirectory = getDataFolder();
        if (!pluginDirectory.exists()){
            pluginDirectory.mkdir();
            treeTypes = new HashSet<>();
        } else {
            try {
                treeTypes = DBManager.loadFile();
            } catch (Exception e) {
                System.out.println("Something went wrong while trying to load data.");
                System.out.println(e.getMessage());
                e.printStackTrace();
                treeTypes = new HashSet<>();
            }
        }

        treeTypes.forEach(treeType -> new BukkitRunnable() {
            @Override
            public void run() {
                treeType.getTrees().forEach(tree -> {
                    if (tree.getGrowthStage() < treeType.getMaxGrowthStage()) {
                        // TODO actually grow in server

                        tree.increaseGrowthStage();
                    }
                });
            }
        }.runTaskTimer(RPGTree.getPlugin(), 0L, 1200 * treeType.getGrowthMinutes()));
    }

    @Override
    public void onDisable() {
        DBManager.updateFile(treeTypes);
    }
}
