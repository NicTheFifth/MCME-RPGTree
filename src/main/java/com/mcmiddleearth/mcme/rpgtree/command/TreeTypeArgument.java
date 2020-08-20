package com.mcmiddleearth.mcme.rpgtree.command;

import com.google.common.collect.Lists;
import com.mcmiddleearth.mcme.rpgtree.RPGTree;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TreeTypeArgument implements ArgumentType<String> {

    static Set<String> options;

    public TreeTypeArgument() {
        options = new HashSet<>();
        RPGTree.getTreeTypes().forEach(treeType ->{
            options.add(treeType.getType());
        });
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public Collection<String> getExamples() {
        return options;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        for (String option : options) {
            if (option.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(option);
            }
        }
        return builder.buildFuture();
    }

    public static void update(){
        RPGTree.getTreeTypes().forEach(treeType ->{
        options.add(treeType.getType());
    });
    }
}
