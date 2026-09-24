package com.idark.valoria.registries.item.types.curio.charm.rune;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.*;
import com.google.common.collect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class CurioStrength extends AbstractRuneItem{
    private final float bonus;
    public CurioStrength(float bonus, Properties properties){
        super(properties);
        this.bonus = bonus;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation uuid, ItemStack stack){
        Multimap<Holder<Attribute>, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, bonus, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return atts;
    }

    @Override
    public RuneType runeType(){
        return RuneType.STRENGTH;
    }
}