package com.idark.valoria.registries;

import net.minecraft.core.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.items.*;
import net.neoforged.neoforge.items.wrapper.*;

import javax.annotation.*;

/**
 * PORT NOTE: NeoForge 21 replaced Forge's {@code getCapability}/{@code LazyOptional} with capability providers
 * registered per block entity type. This reproduces the item-handler exposure the block entities implemented
 * themselves in 1.20.1: jewelry table / keg / manipulator / soul infuser expose the combined inventory for a null side,
 * the output slot from below and the input slots from every other side; the kiln exposes the furnace-style sided
 * wrapper for non-null sides only.
 */
public final class BlockCapabilities{
    private BlockCapabilities(){}

    public static void register(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntitiesRegistry.JEWELRY_BLOCK_ENTITY.get(), (be, side) -> sided(be.itemHandler, be.itemOutputHandler, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntitiesRegistry.KEG_BLOCK_ENTITY.get(), (be, side) -> sided(be.itemHandler, be.itemOutputHandler, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntitiesRegistry.MANIPULATOR_BLOCK_ENTITY.get(), (be, side) -> sided(be.itemHandler, be.itemOutputHandler, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntitiesRegistry.SOUL_INFUSER_BLOCK_ENTITY.get(), (be, side) -> sided(be.itemHandler, be.itemOutputHandler, side));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntitiesRegistry.KILN.get(), (be, side) -> side == null ? null : new SidedInvWrapper(be, side));
    }

    private static IItemHandler sided(ItemStackHandler input, ItemStackHandler output, @Nullable Direction side){
        if(side == null) return new CombinedInvWrapper(input, output);
        return side == Direction.DOWN ? output : input;
    }
}
