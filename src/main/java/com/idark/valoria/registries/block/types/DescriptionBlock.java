package com.idark.valoria.registries.block.types;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DescriptionBlock extends Block{
    public MutableComponent pTooltip;

    public DescriptionBlock(MutableComponent pTooltip, Properties pProperties){
        super(pProperties);
        this.pTooltip = pTooltip;
    }

    // PORT NOTE: appendHoverText takes Item.TooltipContext instead of a nullable BlockGetter in 1.21.
    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltip, TooltipFlag pFlag){
        super.appendHoverText(pStack, pContext, pTooltip, pFlag);
        pTooltip.add(this.pTooltip);
    }
}
