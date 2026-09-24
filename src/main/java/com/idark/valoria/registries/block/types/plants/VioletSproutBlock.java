package com.idark.valoria.registries.block.types.plants;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VioletSproutBlock extends GrowingPlantHeadBlock{
    // PORT NOTE: BlockBehaviour#codec() is abstract in 1.21.
    public static final com.mojang.serialization.MapCodec<VioletSproutBlock> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group(
        propertiesCodec(),
        com.mojang.serialization.Codec.BOOL.fieldOf("glow").forGetter(b -> b.pGlow)
    ).apply(i, VioletSproutBlock::new));
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
    boolean pGlow;

    @Override
    protected com.mojang.serialization.MapCodec<? extends VioletSproutBlock> codec(){
        return CODEC;
    }

    public VioletSproutBlock(BlockBehaviour.Properties p_54300_, boolean pGlow){
        super(p_54300_, Direction.UP, SHAPE, true, 0.14D);
        this.pGlow = pGlow;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource pRandom){
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState pState){
        return pState.isAir();
    }

    @Override
    protected Block getBodyBlock(){
        return pGlow ? BlockRegistry.glowVioletSproutPlant.get() : BlockRegistry.violetSproutPlant.get();
    }
}
