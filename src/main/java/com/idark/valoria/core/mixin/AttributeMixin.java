package com.idark.valoria.core.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Attributes.class})
public class AttributeMixin{
    static{
        // PORT NOTE: Attributes.* are Holder<Attribute> in 1.21; the RangedAttribute instance is the holder's value.
        // (Casting the Holder itself threw ClassCastException at Attributes.<clinit> when the game booted.)
        ((RangedAttributeMixin)Attributes.MAX_HEALTH.value()).setMaxValue(100000.0);
    }
}
