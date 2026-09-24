package com.idark.valoria.core.loot.conditions;

import net.minecraft.core.registries.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class LootConditionsRegistry{
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, "valoria");

    // PORT NOTE: LootItemConditionType wraps a MapCodec instead of a Gson Serializer.
    public static final DeferredHolder<LootItemConditionType, LootItemConditionType> UNLOCKABLE_CONDITION = LOOT_CONDITION_TYPES.register("unlockable", () -> new LootItemConditionType(UnlockableCondition.CODEC));

    public static void init(IEventBus bus) {
        LOOT_CONDITION_TYPES.register(bus);
    }
}
