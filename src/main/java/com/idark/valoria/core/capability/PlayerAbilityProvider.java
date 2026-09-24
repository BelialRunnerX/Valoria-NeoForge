package com.idark.valoria.core.capability;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;

import javax.annotation.*;
import java.util.*;

/**
 * PORT NOTE: the Forge capability provider became a thin accessor for the {@code valoria:ability_tracker} attachment
 * (see {@link ValoriaAttachments#ABILITIES}); the {@code PLAYER_ABILITIES} capability token is gone.
 */
public final class PlayerAbilityProvider {
    private PlayerAbilityProvider(){}

    public static Optional<PlayerAbilityTracker> of(@Nullable Entity entity){
        return entity instanceof Player player ? Optional.of(player.getData(ValoriaAttachments.ABILITIES)) : Optional.empty();
    }
}
