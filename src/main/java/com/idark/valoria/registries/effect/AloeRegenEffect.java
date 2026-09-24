package com.idark.valoria.registries.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

// PORT NOTE: applyEffectTick returns boolean (true = keep the effect); isDurationEffectTick -> shouldApplyEffectTickThisTick.
public class AloeRegenEffect extends MobEffect{

    public AloeRegenEffect(){
        super(MobEffectCategory.BENEFICIAL, 0x6CCE31);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier){
        super.applyEffectTick(entityLivingBaseIn, amplifier);
        if(entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()){
            entityLivingBaseIn.heal(0.025F);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier){
        return true;
    }
}
