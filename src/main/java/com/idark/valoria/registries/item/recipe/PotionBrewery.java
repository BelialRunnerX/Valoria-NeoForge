package com.idark.valoria.registries.item.recipe;

import com.idark.valoria.Valoria;
import com.idark.valoria.registries.EffectsRegistry;
import com.idark.valoria.registries.ItemsRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * PORT NOTE: {@code PotionBrewing.addMix} is no longer static (brewing recipes are per-server in 1.21); the mix is
 * registered through {@link RegisterBrewingRecipesEvent} on the game bus instead of an access-transformed static call.
 */
public class PotionBrewery{
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, Valoria.ID);
    public static final DeferredHolder<Potion, Potion> ALOE_POTION = POTIONS.register("aloe_potion",
            () -> new Potion("aloe_potion", new MobEffectInstance(EffectsRegistry.ALOEREGEN, 3600, 0)));

    public static void bootStrap(){
        NeoForge.EVENT_BUS.addListener(PotionBrewery::onRegisterBrewingRecipes);
    }

    private static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event){
        event.getBuilder().addMix(Potions.WATER, ItemsRegistry.aloePiece.get(), PotionBrewery.ALOE_POTION);
    }

    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
