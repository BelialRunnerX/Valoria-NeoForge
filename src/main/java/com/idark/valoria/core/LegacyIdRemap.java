package com.idark.valoria.core;

import com.idark.valoria.*;
import com.idark.valoria.registries.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import javax.annotation.*;

/**
 * PORT NOTE (behaviour change - feature not available on NeoForge): Forge's {@code MissingMappingsEvent} has no NeoForge 1.21 equivalent (registry remapping was removed with
 * the 1.20.2 registry rewrite), so the old {@code Events#onMissingMappings} handler cannot be wired up. Its mapping
 * table is preserved here so the rename knowledge (shadewood_* -> shade_*, dreadwood_* -> dread_*, ...) is not lost:
 * worlds saved by pre-rename Valoria builds are a 1.20.1-only concern (a 1.20.1 world cannot be opened by this
 * 1.21.1 build without the vanilla upgrade path anyway). Recorded in PORTING.md under "Unverified / unsupported".
 */
public final class LegacyIdRemap{
    private LegacyIdRemap(){}

    @Nullable
    public static Block remapBlock(String oldId){
        switch(oldId){
            case "shadewood" -> { return BlockRegistry.shadeWood.get(); }
            case "stripped_shadewood" -> { return BlockRegistry.strippedShadeWood.get(); }
            case "stripped_shadelog" -> { return BlockRegistry.strippedShadeLog.get(); }
            case "shadelog" -> { return BlockRegistry.shadeLog.get(); }
            case "trapped_shadewood_chest" -> { return BlockRegistry.shadeTrappedChest.get(); }
            case "potted_shadewood_sappling" -> { return BlockRegistry.pottedShadewoodSapling.get(); }
        }

        String newId = renamed(oldId);
        if(newId != null){
            ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(Valoria.ID, newId);
            if(BuiltInRegistries.BLOCK.containsKey(loc)){
                Valoria.LOGGER.error("[REMAP] Remmaping: {} to {}", oldId, newId);
                return BuiltInRegistries.BLOCK.get(loc);
            }
        }

        return null;
    }

    @Nullable
    public static Item remapItem(String oldId){
        switch(oldId){
            case "shadewood" -> { return BlockRegistry.shadeWood.get().asItem(); }
            case "stripped_shadewood" -> { return BlockRegistry.strippedShadeWood.get().asItem(); }
            case "stripped_shadelog" -> { return BlockRegistry.strippedShadeLog.get().asItem(); }
            case "shadelog" -> { return BlockRegistry.shadeLog.get().asItem(); }
            case "trapped_shadewood_chest" -> { return BlockRegistry.shadeTrappedChest.get().asItem(); }
        }

        String newId = renamed(oldId);
        if(newId != null){
            ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(Valoria.ID, newId);
            if(BuiltInRegistries.ITEM.containsKey(loc)){
                Valoria.LOGGER.error("[REMAP] Remmaping: {} to {}", oldId, newId);
                return BuiltInRegistries.ITEM.get(loc);
            }
        }

        return null;
    }

    @Nullable
    private static String renamed(String oldId){
        if(oldId.startsWith("dreadwood_")) return oldId.replace("dreadwood_", "dread_");
        if(oldId.startsWith("shadewood_")) return oldId.replace("shadewood_", "shade_");
        return null;
    }
}
