package com.idark.valoria.registries.block.types.plants;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.BlockRegistry;
import com.idark.valoria.registries.TagsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VoidFlowerBlock extends FlowerBlock{
    // PORT NOTE: FlowerBlock takes a Holder<MobEffect> and the suspicious-stew duration in seconds in 1.21.
    public VoidFlowerBlock(net.minecraft.core.Holder<MobEffect> effect, float effectDurationSeconds, BlockBehaviour.Properties properties){
        super(effect, effectDurationSeconds, properties);
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos){
        Block block = state.getBlock();
        return block == BlockRegistry.voidTaint.get() || block == BlockRegistry.voidGrass.get() || state.is(TagsRegistry.MEAT);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand){
        VoxelShape voxelshape = this.getShape(stateIn, worldIn, pos, CollisionContext.empty());
        Vec3 vector3d = voxelshape.bounds().getCenter();
        double d0 = (double)pos.getX() + vector3d.x;
        double d1 = (double)pos.getZ() + vector3d.z;
        for(int i = 0; i < 3; ++i){
            if(rand.nextBoolean()){
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + rand.nextDouble() / 5.0D, (double)pos.getY() + (0.5D - rand.nextDouble()), d1 + rand.nextDouble() / 5.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}