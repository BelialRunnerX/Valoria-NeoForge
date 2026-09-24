package com.idark.valoria.registries.block.entity;

import com.idark.valoria.client.ui.menus.*;
import com.idark.valoria.registries.*;
import com.idark.valoria.registries.item.recipe.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import pro.komaru.tridot.common.registry.block.entity.*;

/**
 * PORT NOTE: the 1.20.1 kiln block entity was a verbatim copy of AbstractFurnaceBlockEntity (furnace slots, fuel
 * handling, recipe bookkeeping, experience, sided item handler) adapted to Valoria's tick helper. In 1.21 that copy
 * no longer compiles (RecipeHolder, HolderLookup.Provider, capability rewrite, ContainerHelper signatures), so the
 * kiln now extends the vanilla furnace base directly, which provides the identical behaviour; only the recipe type,
 * name and menu are Valoria's. The item handler capability is registered in {@link BlockCapabilities}.
 */
public class KilnBlockEntity extends AbstractFurnaceBlockEntity implements TickableBlockEntity{
    public KilnBlockEntity(BlockPos pPos, BlockState pBlockState){
        super(BlockEntitiesRegistry.KILN.get(), pPos, pBlockState, KilnRecipe.Type.INSTANCE);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.valoria.kiln");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new KilnMenu(pId, pPlayer, this, this.dataAccess);
    }

    @Override
    public void tick(){
        if(this.level != null && !this.level.isClientSide){
            serverTick(this.level, this.worldPosition, this.getBlockState(), this);
        }
    }
}
