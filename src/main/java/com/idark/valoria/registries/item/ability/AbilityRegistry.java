package com.idark.valoria.registries.item.ability;

import net.minecraft.core.registries.*;
import net.minecraft.resources.*;

import javax.annotation.*;
import java.util.*;

public class AbilityRegistry{
    private static final Map<ResourceLocation, AbilityType<?>> REGISTRY = new HashMap<>();
    public static <T extends AbilityType<?>> T register(T type) {
        // PORT NOTE: when a client resource reload fails, Minecraft 1.21 retries it and NeoForge dispatches FMLCommonSetupEvent a
        // second time. Registering the very same AbilityType instance again is therefore a no-op instead of a crash; a
        // *different* type claiming an existing id is still rejected exactly as before.
        AbilityType<?> existing = REGISTRY.get(type.id);
        if (existing == type) {
            return type;
        }
        if (existing != null) {
            throw new IllegalArgumentException("Ability Type " + type.id + " is already registered!");
        }
        REGISTRY.put(type.id, type);
        return type;
    }

    @Nullable
    public static AbilityType<?> getType(ResourceLocation id) {
        return REGISTRY.get(id);
    }
}
