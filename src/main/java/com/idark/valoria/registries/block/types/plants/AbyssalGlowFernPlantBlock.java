package com.idark.valoria.registries.block.types.plants;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.BlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.phys.shapes.Shapes;

public class AbyssalGlowFernPlantBlock extends GrowingPlantBodyBlock{
    public static final com.mojang.serialization.MapCodec<AbyssalGlowFernPlantBlock> CODEC = simpleCodec(AbyssalGlowFernPlantBlock::new); // PORT NOTE: BlockBehaviour#codec() is abstract in 1.21
    @Override protected com.mojang.serialization.MapCodec<? extends AbyssalGlowFernPlantBlock> codec(){ return CODEC; }


    public AbyssalGlowFernPlantBlock(Properties pProperties){
        super(pProperties, Direction.UP, Shapes.block(), true);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock(){
        return (GrowingPlantHeadBlock)BlockRegistry.abyssalGlowfern.get();
    }
}