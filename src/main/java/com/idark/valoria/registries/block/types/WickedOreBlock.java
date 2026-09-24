package com.idark.valoria.registries.block.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WickedOreBlock extends Block{
    private final IntProvider xpRange;

    public WickedOreBlock(BlockBehaviour.Properties properties){
        this(properties, ConstantInt.of(0));
    }

    public WickedOreBlock(BlockBehaviour.Properties properties, IntProvider pXpRange){
        super(properties);
        this.xpRange = pXpRange;
    }

    public void spawnAfterBreak(BlockState pState, ServerLevel pLevel, BlockPos pPos, ItemStack pStack, boolean pDropExperience){
        super.spawnAfterBreak(pState, pLevel, pPos, pStack, pDropExperience);
    }

    /**
     * PORT NOTE: IBlockExtension#getExpDrop signature changed in NeoForge 21. The explicit "silk touch level == 0" check is
     * gone because NeoForge now runs the result through {@code EnchantmentHelper.processBlockExperience} (BlockDropsEvent),
     * where silk touch's {@code block_experience} effect zeroes it, exactly as for vanilla ores.
     */
    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool){
        return this.xpRange.sample(level.getRandom());
    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn){
        RandomSource rand = worldIn.getRandom();
        for(int i = 0; i < 5; i++){
            worldIn.addParticle(ParticleTypes.REVERSE_PORTAL, pos.getX() + rand.nextDouble(), pos.getY() + 0.5D, pos.getZ() + rand.nextDouble(), 0d, 0.05d, 0d);
        }
    }

    @Override
    public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state){
        RandomSource rand = worldIn.getRandom();
        for(int i = 0; i < 5; i++){
            worldIn.addParticle(ParticleTypes.REVERSE_PORTAL, pos.getX() + rand.nextDouble(), pos.getY() + 0.5D, pos.getZ() + rand.nextDouble(), 0d, 0.05d, 0d);
        }
    }
}
