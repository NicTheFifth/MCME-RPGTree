package com.mcmiddleearth.mcme.rpgtree.command;

import com.boydti.fawe.object.io.PGZIPOutputStream;
import com.mcmiddleearth.mcme.rpgtree.RPGTree;
import com.mcmiddleearth.mcme.rpgtree.TreeType;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.FastSchematicWriter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import java.io.*;

public class TreeCommand extends CommandDispatcher<Player>{
    public TreeCommand() {
        register(LiteralArgumentBuilder.<Player>literal("rpgt")
            .then(LiteralArgumentBuilder.<Player>literal("type")
                .then(LiteralArgumentBuilder.<Player>literal("new")
                     .then(RequiredArgumentBuilder.<Player, String>argument("treeType", new CommandStringVariableArgument()).executes( c->{
                        doCommand("newTreeType", c.getArgument("treeType", String.class), c.getSource());
                        return 1;
                     })))
                .then(LiteralArgumentBuilder.<Player>literal("list").executes(c ->{
                    doCommand("getTreeTypes", c.getSource());
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<Player>literal("maxgrowth")
                    .then(RequiredArgumentBuilder.<Player, String>argument("treeType", new TreeTypeArgument())
                        .then(RequiredArgumentBuilder.<Player, String>argument("maxGrowth", new CommandIntVariableArgument()).executes( c ->{
                            doCommand("setMaxGrowth", c.getArgument("treeType", String.class), c.getArgument("maxGrowth", String.class), c.getSource());
                            return 1;
                        }))
                    )
                )
                .then(LiteralArgumentBuilder.<Player>literal("growthmin")
                    .then(RequiredArgumentBuilder.<Player, String>argument("treeType", new TreeTypeArgument())
                        .then(RequiredArgumentBuilder.<Player, String>argument("growthTime", new CommandIntVariableArgument()).executes( c ->{
                            doCommand("setGrowthTime", c.getArgument("treeType", String.class), c.getArgument("growthTime", String.class), c.getSource());
                            return 1;
                        }))
                    )
                )
                .then(LiteralArgumentBuilder.<Player>literal("setstate")
                    .then(RequiredArgumentBuilder.<Player, String>argument("treeType", new TreeTypeArgument())
                        .then(RequiredArgumentBuilder.<Player, String>argument("state", new CommandIntVariableArgument()).executes( c ->{
                            doCommand("setGrowthState", c.getArgument("treeType", String.class), c.getArgument("state", String.class), c.getSource());
                            return 1;
                        }))
                    )
                )
            )
        );
    }

    private void doCommand(String action, Player source) {
        switch(action){
            case "getTreeTypes":
                source.sendMessage(ChatColor.BLUE + "Tree types are: \n");
                RPGTree.getTreeTypes().forEach(treeType -> {
                    source.sendMessage(treeType.getType() + "\n");
                        });
                break;
        }
    }

    private void doCommand(String action, String argument, Player source) {
        switch(action){
            case "newTreeType":
                TreeType newTree = new TreeType();
                newTree.setType(argument);
                RPGTree.getTreeTypes().add(newTree);
                TreeTypeArgument.update();
                break;
        }
    }

    private void doCommand(String action, String argument1, String argument2, Player source) {
        switch(action){
            case "setMaxGrowth":
                RPGTree.getTreeTypes().stream().filter(treeType -> argument1.equalsIgnoreCase(treeType.getType())).forEach(treeType -> {
                    treeType.setMaxGrowthStage(Integer.parseInt(argument2));
                    if(treeType.hasAllStates())
                        source.sendMessage(ChatColor.RED + argument1 + " is missing frames.");
                });
                break;
            case "setGrowthTime":
                RPGTree.getTreeTypes().stream().filter(treeType -> argument1.equalsIgnoreCase(treeType.getType())).forEach(treeType -> {
                    treeType.setGrowthMinutes(Integer.parseInt(argument2));
                });
                break;
            case "setGrowthState":
                File file = new File(RPGTree.getGrowthStateDirectory()+ RPGTree.getFileSep() + argument1 + "_" + argument2);
                LocalSession session = RPGTree.getLocalSession().findByName(source.getName());

                    Region region = session.getSelection((World) source.getWorld());

                    Clipboard clipboard = Clipboard.create(region);
                        try (OutputStream stream = new FileOutputStream(file); NBTOutputStream output = new NBTOutputStream(new BufferedOutputStream(new PGZIPOutputStream(stream)))) {
                            new FastSchematicWriter(output).write(clipboard);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                break;
        }
    }
}