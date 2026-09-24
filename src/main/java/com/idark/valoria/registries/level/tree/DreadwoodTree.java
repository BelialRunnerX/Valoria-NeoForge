package com.idark.valoria.registries.level.tree;

import com.idark.valoria.registries.level.*;
import net.minecraft.world.level.block.grower.*;

import java.util.*;

/** PORT NOTE: AbstractMegaTreeGrower -> TreeGrower record in 1.21; {@link #INSTANCE} replaces {@code new DreadwoodTree()}. */
public final class DreadwoodTree{
    public static final TreeGrower INSTANCE = new TreeGrower("valoria:dreadwood", Optional.of(LevelGen.FANCY_DREADWOOD_TREE), Optional.of(LevelGen.DREADWOOD_TREE), Optional.empty());

    private DreadwoodTree(){}
}
