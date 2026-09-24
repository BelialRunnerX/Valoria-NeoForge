package com.idark.valoria.registries.item.types.curio.charm.rune;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.*;
import com.google.common.collect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class CurioWealth extends AbstractRuneItem{
    private final float luck;
    public CurioWealth(float luck, Properties properties){
        super(properties);
        this.luck = luck;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation uuid, ItemStack stack){
        Multimap<Holder<Attribute>, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(Attributes.LUCK, new AttributeModifier(uuid, luck, AttributeModifier.Operation.ADD_VALUE));
        return atts;
    }

    @Override
    public RuneType runeType(){
        return RuneType.WEALTH;
    }
}