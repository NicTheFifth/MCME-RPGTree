package com.mcmiddleearth.mcme.rpgtree;

import lombok.Getter;
import lombok.Setter;

public class Tree {
    @Getter private Integer growthState;
    @Getter @Setter private EventLocation treeLocation;
    @Getter @Setter private Integer treeType;
    public void grow(){
        growthState ++;
    }
}
