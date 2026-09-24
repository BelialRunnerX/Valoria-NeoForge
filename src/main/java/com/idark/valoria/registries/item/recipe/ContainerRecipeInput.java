package com.idark.valoria.registries.item.recipe;

import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.items.*;

/**
 * PORT NOTE: recipes take a {@link RecipeInput} instead of a {@code Container} in 1.21. Valoria's machines keep their
 * {@code Container}/{@code IItemHandler} inventories and wrap them with this view when matching or assembling.
 */
public record ContainerRecipeInput(Container container) implements RecipeInput{
    public static ContainerRecipeInput of(Container container){
        return new ContainerRecipeInput(container);
    }

    public static ContainerRecipeInput of(IItemHandler handler){
        SimpleContainer container = new SimpleContainer(handler.getSlots());
        for(int i = 0; i < handler.getSlots(); i++){
            container.setItem(i, handler.getStackInSlot(i));
        }

        return new ContainerRecipeInput(container);
    }

    @Override
    public ItemStack getItem(int index){
        return index < container.getContainerSize() ? container.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public int size(){
        return container.getContainerSize();
    }
}
