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

import com.google.common.base.Joiner;
import com.mcmiddleearth.mcme.rpgtree.command.TreeCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.session.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RPGTree extends JavaPlugin {

    @Getter
    private static RPGTree plugin;
    @Getter @Setter
    private static Set<TreeType> treeTypes;
    @Getter
    private static File pluginDirectory;
    @Getter
    private static File growthStateDirectory;
    @Getter
    private static String FileSep = System.getProperty("file.separator");
    private static CommandDispatcher<Player> commandDispatcher;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            try {
                if (args.length > 0) {
                    commandDispatcher.execute(commandDispatcher.parse(String.format("%s %s", label, Joiner.on(" ").join(args)), (Player) sender));
                } else {
                    commandDispatcher.execute(commandDispatcher.parse(label, (Player) sender));
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            try {
                CompletableFuture<Suggestions> completionSuggestions = commandDispatcher.getCompletionSuggestions(commandDispatcher.parse(getInput(command, args), (Player) sender));
                return completionSuggestions.get().getList().stream().map(Suggestion::getText).collect(Collectors.toList());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    private String getInput(Command command, String[] args) {
        StringBuilder input = new StringBuilder(command.getName());
        for (String arg : args) {
            input.append(CommandDispatcher.ARGUMENT_SEPARATOR).append(arg);
        }
        return input.toString();
    }

    @Override
    public void onEnable() {
        Logger.getLogger("RPGTree").log(Level.INFO, "RPGTree loaded correctly");
        plugin = this;

        this.pluginDirectory = getDataFolder();
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
        this.growthStateDirectory = new File(getPluginDirectory() + FileSep + "growthStates");
        if(!growthStateDirectory.exists())
            growthStateDirectory.mkdir();

        commandDispatcher = new TreeCommand();

        treeTypes.forEach(treeType -> new BukkitRunnable() {
            @Override
            public void run() {
                treeType.getTrees().forEach(tree -> {
                    if (tree.getGrowthStage() < treeType.getMaxGrowthStage()) {
                        // TODO actually grow in server

                        tree.increaseGrowthStage(treeType.getType());
                    }
                });
            }
        }.runTaskTimer(RPGTree.getPlugin(), 0L, 1200 * treeType.getGrowthMinutes()));
    }

    @Override
    public void onDisable() {
        DBManager.updateFile(treeTypes);
    }

    public static SessionManager getLocalSession(){
        return WorldEdit.getInstance().getSessionManager();
    }
}
