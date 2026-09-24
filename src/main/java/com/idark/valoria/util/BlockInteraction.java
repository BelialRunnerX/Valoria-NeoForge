package com.idark.valoria.util;

import net.minecraft.world.*;

/**
 * PORT NOTE: {@code Block#use} was split in 1.20.5 into {@code useWithoutItem} (empty hand) and {@code useItemOn}
 * (held item, returning {@link ItemInteractionResult}). Valoria's blocks keep their single {@code interact} method and
 * route both hooks through it; this maps the old result to the item-hook result. PASS becomes
 * SKIP_DEFAULT_BLOCK_INTERACTION so the interaction runs once and then falls through to the item, as in 1.20.1.
 */
public final class BlockInteraction{
    private BlockInteraction(){}

    public static ItemInteractionResult toItemResult(InteractionResult result){
        return switch(result){
            case SUCCESS, SUCCESS_NO_ITEM_USED -> ItemInteractionResult.SUCCESS;
            case CONSUME -> ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL;
            case FAIL -> ItemInteractionResult.FAIL;
            case PASS -> ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        };
    }
}
