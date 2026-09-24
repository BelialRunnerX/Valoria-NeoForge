package com.idark.valoria.core.interfaces;

import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public interface IBreakableCurio{
    // PORT NOTE: ItemStack#hurtAndBreak(int, LivingEntity, Consumer<LivingEntity>) became (int, ServerLevel, ServerPlayer, Consumer<Item>);
    // the break callback no longer receives the entity, so the wearer is captured from the parameter instead.
    default void accessoryHurt(LivingEntity target, ItemStack stack){
        if(!(target.level() instanceof ServerLevel serverLevel)) return;
        stack.hurtAndBreak(1, serverLevel, target instanceof ServerPlayer serverPlayer ? serverPlayer : null, (item) -> {
            LivingEntity entity = target;
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
            SoundEvents.ITEM_BREAK, entity.getSoundSource(), 0.8f, 0.8f + entity.level().random.nextFloat() * 0.4f);
            serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), entity.getX(), entity.getY() + 1.0, entity.getZ(), 10, 0.2, 0.2, 0.2, 0.05);
        });
    }
}
