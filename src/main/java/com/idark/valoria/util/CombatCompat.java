package com.idark.valoria.util;

import com.idark.valoria.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;

/**
 * PORT NOTE: the 1.20.1 {@code EnchantmentHelper} level lookups used by Valoria's weapon abilities were removed with
 * the data-driven enchantments of 1.21. These helpers reproduce the old numbers:
 * <ul>
 *   <li>{@code getSweepingDamageRatio(entity)} was {@code 1 - 1/(lvl+1)}; that ratio is now the
 *       {@link Attributes#SWEEPING_DAMAGE_RATIO} attribute Sweeping Edge writes on the wielder.</li>
 *   <li>{@code getFireAspect(entity)} is the Fire Aspect level on the main-hand stack.</li>
 *   <li>{@code getDamageBonus(stack, mobType)} (Sharpness/Smite/Bane of Arthropods) is what
 *       {@link EnchantmentHelper#modifyDamage} adds on top of a zero base damage; it needs a server level, so on the
 *       client the bonus is 0 exactly as the old code effectively was for client-side hits.</li>
 * </ul>
 */
public final class CombatCompat{
    private CombatCompat(){}

    public static float sweepingRatio(LivingEntity entity){
        AttributeInstance instance = entity.getAttribute(Attributes.SWEEPING_DAMAGE_RATIO);
        return instance == null ? 0f : (float)instance.getValue();
    }

    public static int fireAspect(LivingEntity entity){
        return EnchantmentsRegistry.getLevel(entity.level(), entity.getMainHandItem(), Enchantments.FIRE_ASPECT);
    }

    public static float damageBonus(Level level, ItemStack weapon, Entity target, DamageSource source){
        if(level instanceof ServerLevel server && !weapon.isEmpty()){
            return EnchantmentHelper.modifyDamage(server, weapon, target, source, 0f);
        }

        return 0f;
    }

    /** Damage bonus for a player melee hit; the usual call site shape {@code damage + getDamageBonus(stack, target.getMobType())}. */
    public static float damageBonus(Player player, ItemStack weapon, Entity target){
        return damageBonus(player.level(), weapon, target, player.damageSources().playerAttack(player));
    }
}
