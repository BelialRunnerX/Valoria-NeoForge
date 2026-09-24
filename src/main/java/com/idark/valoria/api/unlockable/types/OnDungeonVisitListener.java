package com.idark.valoria.api.unlockable.types;

import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.levelgen.structure.*;

public interface OnDungeonVisitListener{
    void checkCondition(ServerPlayer player, ServerLevel serverLevel);

    // PORT NOTE: StructureManager#getStructureWithPieceAt no longer accepts a ResourceKey; resolve the Structure first.
    default boolean isPlayerInStructure(Player player, ServerLevel serverLevel, ResourceKey<Structure> structureKey) {
        Structure structureType = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).get(structureKey);
        if(structureType == null) return false;
        var structure = serverLevel.structureManager().getStructureWithPieceAt(player.blockPosition(), structureType);
        // PORT NOTE (upstream bug fix): outside a matching structure this is StructureStart.INVALID_START whose getBoundingBox() throws ("Unable to
        // calculate boundingbox without pieces"). 1.20.1 had the same hole but the caller (CapabilityEvents.onServerTick) never ran.
        if(!structure.isValid()) return false;
        return structure.getBoundingBox().isInside(player.getBlockX(), player.getBlockY(), player.getBlockZ());
    }

    default boolean isPlayerInStructure(Player player, ServerLevel serverLevel, TagKey<Structure> tag) {
        var structure = serverLevel.structureManager().getStructureWithPieceAt(player.blockPosition(), tag);
        if(!structure.isValid()) return false; // PORT NOTE (upstream bug fix): see above
        return structure.getBoundingBox().isInside(player.getBlockX(), player.getBlockY(), player.getBlockZ());
    }
}
