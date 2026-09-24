package com.idark.valoria.core.interfaces;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

/**
 * PORT NOTE: {@code DyeableLeatherItem} was removed in 1.20.5; dye colour is the {@code minecraft:dyed_color} component
 * and dye-ability is the {@code minecraft:dyeable} item tag (the datagen adds every implementor to it). This marker keeps
 * the {@code instanceof}/{@code getColor} call sites of the 1.20.1 code working.
 */
public interface DyeableItem{
    int DEFAULT_LEATHER_COLOR = DyedItemColor.LEATHER_COLOR;

    default int getColor(ItemStack stack){
        return DyedItemColor.getOrDefault(stack, DEFAULT_LEATHER_COLOR);
    }

    default boolean hasCustomColor(ItemStack stack){
        return stack.has(net.minecraft.core.component.DataComponents.DYED_COLOR);
    }

    default void setColor(ItemStack stack, int color){
        stack.set(net.minecraft.core.component.DataComponents.DYED_COLOR, new DyedItemColor(color, true));
    }

    default void clearColor(ItemStack stack){
        stack.remove(net.minecraft.core.component.DataComponents.DYED_COLOR);
    }
}
