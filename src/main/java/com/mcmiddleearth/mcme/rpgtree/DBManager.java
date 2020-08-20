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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bukkit.Location;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DBManager {

    @SuppressWarnings("unchecked")
    public static void updateFile(Set<TreeType> treeTypes){
        JSONArray treeTypesJSON = new JSONArray();
        JSONObject treeTypeJSON = new JSONObject();
        JSONArray treesJSON = new JSONArray();
        JSONObject treeJSON = new JSONObject();
        treeTypes.forEach(treeType -> {
            treeTypeJSON.put("type", treeType.getType());
            treeTypeJSON.put("maxGrowthStage", treeType.getMaxGrowthStage());
            treeTypeJSON.put("growthMinutes", treeType.getGrowthMinutes());
            treeType.getTrees().forEach(tree -> {
                treeJSON.put("x", tree.getLoc().getX());
                treeJSON.put("y", tree.getLoc().getY());
                treeJSON.put("z", tree.getLoc().getZ());
                treeJSON.put("growthStage", tree.getGrowthStage());
                treesJSON.add(treeJSON);
            });
            treeTypeJSON.put("trees", treesJSON);
            treeTypesJSON.add(treeTypeJSON);
        });

        try (FileWriter file = new FileWriter(RPGTree.getPluginDirectory() + RPGTree.getFileSep() + "data.json")) {

            file.write(treeTypesJSON.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Set<TreeType> loadFile() throws Exception {
        Set<TreeType> treeTypes = new HashSet<>();

        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(RPGTree.getPluginDirectory() + RPGTree.getFileSep() + "data.json"))
        {
            JSONArray treeTypesJSON = (JSONArray) jsonParser.parse(reader);
            treeTypesJSON.forEach(treeType -> {
                JSONObject treeTypeJSON = (JSONObject) treeType;
                TreeType tempTreeType = new TreeType();
                tempTreeType.setType((String) treeTypeJSON.get("type"));
                tempTreeType.setMaxGrowthStage((Integer) treeTypeJSON.get("maxGrowthStage"));
                tempTreeType.setGrowthMinutes((Integer) treeTypeJSON.get("growthMinutes"));

                JSONArray treesJSON = (JSONArray) treeTypeJSON.get("trees");
                treesJSON.forEach(tree -> {
                    JSONObject treeJSON = (JSONObject) tree;
                    Tree tempTree = new Tree();
                    tempTree.setGrowthStage((Integer) treeJSON.get("growthStage"));
                    tempTree.setLoc(new Location(Bukkit.getWorld("world"), (double) treeJSON.get("x"), (double) treeJSON.get("y"), (double) treeJSON.get("z")));
                    tempTreeType.getTrees().add(tempTree);

                });
                treeTypes.add(tempTreeType);
            });

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (ParseException e) {
            throw new ParseException(e.getErrorType());
        }

        return treeTypes;
    }

}
