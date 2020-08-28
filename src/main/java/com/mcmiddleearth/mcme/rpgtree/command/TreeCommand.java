package com.mcmiddleearth.mcme.rpgtree.command;

import com.boydti.fawe.object.io.PGZIPOutputStream;
import com.mcmiddleearth.mcme.rpgtree.RPGTree;
import com.mcmiddleearth.mcme.rpgtree.TreeType;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.FastSchematicWriter;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TreeCommand extends CommandDispatcher<Player>{
    public TreeCommand() {
        register(LiteralArgumentBuilder.<Player>literal("rpgt")
            .then(LiteralArgumentBuilder.<Player>literal("type")
                .then(LiteralArgumentBuilder.<Player>literal("new")
                     .then(RequiredArgumentBuilder.<Player, String>argument("treeType", new NewTreeTypeArgument()).executes( c->{
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
            .then(LiteralArgumentBuilder.<Player>literal("newtree")
                .then(RequiredArgumentBuilder.<Player, String>argument( "treeType", new TreeTypeArgument()).executes(c -> {
                    doCommand("newTree", c.getArgument("treeType", String.class), c.getSource());
                    return 1;
                }))
            )
            .then(LiteralArgumentBuilder.<Player>literal("deleteTree")
                .then(RequiredArgumentBuilder.<Player, String>argument("treeType", new TreeTypeArgument())
                    .then(RequiredArgumentBuilder.<Player, String>argument("tree", new CommandIntVariableArgument()).executes(c->{
                        doCommand("delTree", c.getArgument("treeType", String.class), c.getArgument("tree", String.class), c.getSource());
                        return 1;
                    }))
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
            case "log":
                Logger.getLogger("MCME-RPGTree").log(Level.INFO, argument + " has been logged.");
            case "newTreeType":
                TreeType newTree = new TreeType();
                newTree.setType(argument);
                RPGTree.getTreeTypes().add(newTree);
                TreeTypeArgument.update();
                source.sendMessage(ChatColor.GREEN + "New type " + argument + " has been made.");
                break;
            case "newTree":
                RPGTree.getTreeTypes().stream().filter(treeType -> argument.equalsIgnoreCase(treeType.getType())).forEach(treeType ->{
                    treeType.addTree(source);
                    source.sendMessage(ChatColor.GREEN + "New tree for " + argument + " has been made.");
                });
                break;
        }
    }

    private void doCommand(String action, String argument1, String argument2, Player source) {
        switch(action){
            case "setMaxGrowth":
                RPGTree.getTreeTypes().stream().filter(treeType -> argument1.equalsIgnoreCase(treeType.getType())).forEach(treeType -> {
                    treeType.setMaxGrowthStage(Long.valueOf(argument2));
                    if(treeType.hasAllStates())
                        source.sendMessage(ChatColor.RED + argument1 + " is missing frames.");
                });
                source.sendMessage(ChatColor.GRAY + "Max growth set");
                break;
            case "setGrowthTime":
                RPGTree.getTreeTypes().stream().filter(treeType -> argument1.equalsIgnoreCase(treeType.getType())).forEach(treeType -> {
                    treeType.setGrowthMinutes(Long.valueOf(argument2));
                });
                source.sendMessage(ChatColor.GRAY + "Growth time set");
                break;
            case "setGrowthState":
                File file = new File(RPGTree.getGrowthStateDirectory()+ RPGTree.getFileSep() + argument1 + "_" + argument2 + ".schem");
                LocalSession session = RPGTree.getLocalSession().findByName(source.getDisplayName());

                Region region = session.getSelection(session.getSelectionWorld());
                if(region != null) {
                    Clipboard clipboard = Clipboard.create(region);
                    try (OutputStream stream = new FileOutputStream(file); NBTOutputStream output = new NBTOutputStream(new BufferedOutputStream(new PGZIPOutputStream(stream)))) {
                        new FastSchematicWriter(output).write(clipboard);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    source.sendMessage(ChatColor.GRAY + "Growth state set");
                } else
                    source.sendMessage(ChatColor.RED + "Be sure to make a selection");
                break;
            case "delTree":
                RPGTree.getTreeTypes().stream().filter(treeType -> argument1.equalsIgnoreCase(treeType.getType())).forEach(treeType -> {
                    treeType.removeTree(Integer.parseInt(argument2));
                });
                break;
        }
    }
}