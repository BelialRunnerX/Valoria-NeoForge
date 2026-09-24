package com.idark.valoria.core.datagen;

import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.data.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;

import java.util.*;
import java.util.concurrent.*;

// PORT NOTE: LootTableProvider takes the registry lookup future, sub providers are built from a HolderLookup.Provider,
// and validate() now receives the WritableRegistry / ProblemReporter. Validation is still skipped on purpose.
public class LootTableGen{

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries){
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(LootTableSubprovider::new, LootContextParamSets.BLOCK)
        ), registries) {
            @Override
            protected void validate(net.minecraft.core.WritableRegistry<net.minecraft.world.level.storage.loot.LootTable> writableregistry, net.minecraft.world.level.storage.loot.ValidationContext validationcontext, net.minecraft.util.ProblemReporter.Collector problemreporter) {
                // Skip validation so it doesn't crash on blocks with missing datagen tables
            }
        };
    }
}
