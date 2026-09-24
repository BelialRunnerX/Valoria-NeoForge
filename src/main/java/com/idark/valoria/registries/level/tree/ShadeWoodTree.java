package com.idark.valoria.registries.level.tree;

import com.idark.valoria.registries.level.LevelGen;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

/**
 * PORT NOTE: AbstractMegaTreeGrower was replaced by the {@link TreeGrower} record in 1.21 (the 2x2 "mega" feature and
 * the single-sapling feature are given as configured-feature keys); {@link #INSTANCE} replaces {@code new ShadeWoodTree()}.
 */
public final class ShadeWoodTree{
    public static final TreeGrower INSTANCE = new TreeGrower("valoria:shadewood", Optional.of(LevelGen.FANCY_SHADEWOOD_TREE), Optional.of(LevelGen.SHADEWOOD_TREE), Optional.empty());

    private ShadeWoodTree(){}
}
