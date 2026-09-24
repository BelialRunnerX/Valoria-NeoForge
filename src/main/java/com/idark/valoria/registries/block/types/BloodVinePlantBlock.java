package com.idark.valoria.registries.block.types;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BloodVinePlantBlock extends GrowingPlantBodyBlock{
    public static final com.mojang.serialization.MapCodec<BloodVinePlantBlock> CODEC = simpleCodec(BloodVinePlantBlock::new); // PORT NOTE: BlockBehaviour#codec() is abstract in 1.21
    @Override protected com.mojang.serialization.MapCodec<? extends BloodVinePlantBlock> codec(){ return CODEC; }

    public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public BloodVinePlantBlock(BlockBehaviour.Properties p_154975_){
        super(p_154975_, Direction.DOWN, SHAPE, false);
    }

    protected GrowingPlantHeadBlock getHeadBlock(){
        return (GrowingPlantHeadBlock)BlockRegistry.bloodVine.get();
    }
}
