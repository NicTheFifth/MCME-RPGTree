package com.mcmiddleearth.mcme.rpgtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import lombok.Getter;

public class RPGTree extends JavaPlugin{

    @Getter private static RPGTree plugin;
    public Map<TreeType, List<Tree>> treeTypeListHashMap = new HashMap<>();

    @Override
    public void onEnable(){
        Logger.getLogger("RPGTree").log(Level.INFO,"RPGTree loaded correctly");
        plugin = this;

        new BukkitRunnable() {
            @Override
            public void run() {
            //treeTypeListHashMap
            }
        }.runTaskTimer(RPGTree.getPlugin(), 0L, 1200 );
    }
    @Override
    public void onDisable() {

    }
}
