package com.idark.valoria.registries.block.types;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.block.entity.ModSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

import javax.annotation.Nullable;

public class ModWallSignBlock extends WallSignBlock{
    public ModWallSignBlock(Properties properties, WoodType type){
        super(type, properties); // PORT NOTE: sign ctors take (WoodType, Properties) in 1.21
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState){
        return new ModSignBlockEntity(pPos, pState);
    }
}