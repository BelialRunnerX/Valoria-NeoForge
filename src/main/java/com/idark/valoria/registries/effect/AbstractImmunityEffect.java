package com.idark.valoria.registries.effect;

import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;

import java.util.function.*;

/**
 * PORT NOTE: MobEffect#applyEffectTick returns a boolean in 1.21; returning {@code false} makes the entity remove the
 * effect instance, which replaces the explicit {@code pEntity.removeEffect(this)} of 1.20.1 (effects are referenced by
 * Holder now and an effect does not know its own holder).
 */
public abstract class AbstractImmunityEffect extends MobEffect{
    protected AbstractImmunityEffect(MobEffectCategory pCategory, int pColor){
        super(pCategory, pColor);
    }

    public abstract Predicate<ItemStack> getImmunityItem();

    public boolean effectRemoveReason(LivingEntity pEntity) {
        return CuriosApi.getCuriosHelper().findEquippedCurio(getImmunityItem(), pEntity).isPresent();
    }

    @Override
    public boolean applyEffectTick(LivingEntity pEntity, int amplifier){
        return !effectRemoveReason(pEntity);
    }
}
