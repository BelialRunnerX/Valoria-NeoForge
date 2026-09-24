package com.idark.valoria.registries.level.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * PORT NOTE: Forge's {@code ITeleporter} does not exist in NeoForge 1.21; dimension travel goes through the vanilla
 * {@code Portal} interface which returns a {@code DimensionTransition}. This class keeps the shared state/helpers of the
 * old teleporter (position, inside-dimension flag, PoI key, frame height search); the entity placement that used to live
 * in {@code placeEntity} is now the transition's post-transition callback ({@code Entity#setPortalCooldown}).
 */
public class BaseTeleporter{
    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideDimension = true;
    protected static ResourceKey<PoiType> poi;

    public BaseTeleporter(BlockPos pos, boolean insideDim, ResourceKey<PoiType> pPoi){
        thisPos = pos;
        insideDimension = insideDim;
        poi = pPoi;
    }

    public boolean isVanilla(){
        return false;
    }

    protected int getHeight(ServerLevel level, int height, int posX, int posZ, Block pBlock){
        for(int y = level.getHeight(); y > height; y--){
            BlockState block = level.getBlockState(new BlockPos(posX, y, posZ));
            if(block.is(pBlock)){
                return y;
            }
            if(block.is(pBlock)){
                return ++y;
            }
        }
        return level.getHeight(Heightmap.Types.MOTION_BLOCKING, posX, posZ);
    }

    public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld){
        return false;
    }
}
