package com.idark.valoria.core.capability;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;

import javax.annotation.*;
import java.util.*;

public interface INihilityLevel{
    /**
     * PORT NOTE: replaces the {@code Capability<INihilityLevel> INSTANCE} token. Returns the player's attachment
     * (present for every player, like the old capability that was attached to all players) or empty for other entities.
     */
    static Optional<INihilityLevel> of(@Nullable Entity entity){
        return entity instanceof Player player ? Optional.of(player.getData(ValoriaAttachments.NIHILITY)) : Optional.empty();
    }

    void modifyAmount(@Nullable LivingEntity entity, float amount);

    void decrease(@Nullable LivingEntity entity, float amount);

    void setAmount(float amount);

    void setAmountFromServer(@Nullable LivingEntity entity, float amount);

    float getAmount() ;

    void setMaxAmount(float amount);

    float getMaxAmount(@Nullable LivingEntity entity);

    void copyFrom(INihilityLevel source);
}
