package com.idark.valoria.registries.level.tree;

import com.idark.valoria.registries.level.LevelGen;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

/** PORT NOTE: AbstractMegaTreeGrower -> TreeGrower record in 1.21; {@link #INSTANCE} replaces {@code new EldritchTree()}. */
public final class EldritchTree{
    public static final TreeGrower INSTANCE = new TreeGrower("valoria:eldritch", Optional.of(LevelGen.FANCY_ELDRITCH_TREE), Optional.of(LevelGen.ELDRITCH_TREE), Optional.empty());

    private EldritchTree(){}
}
