package com.idark.valoria.registries.block.types.plants;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.phys.shapes.Shapes;

public class VioletSproutPlantBlock extends GrowingPlantBodyBlock{
    // PORT NOTE: BlockBehaviour#codec() is abstract in 1.21.
    public static final com.mojang.serialization.MapCodec<VioletSproutPlantBlock> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group(
        propertiesCodec(),
        com.mojang.serialization.Codec.BOOL.fieldOf("glow").forGetter(b -> b.pGlow)
    ).apply(i, VioletSproutPlantBlock::new));

    boolean pGlow;

    @Override
    protected com.mojang.serialization.MapCodec<? extends VioletSproutPlantBlock> codec(){
        return CODEC;
    }

    public VioletSproutPlantBlock(Properties pProperties, Boolean pGlow){
        super(pProperties, Direction.UP, Shapes.block(), true);
        this.pGlow = pGlow;
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock(){
        return pGlow ? (GrowingPlantHeadBlock)BlockRegistry.glowVioletSprout.get() : (GrowingPlantHeadBlock)BlockRegistry.violetSprout.get();
    }
}
