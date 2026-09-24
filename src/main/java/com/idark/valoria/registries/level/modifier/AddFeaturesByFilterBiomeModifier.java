package com.idark.valoria.registries.level.modifier;

import net.minecraft.core.registries.*;
import com.idark.valoria.registries.level.LevelGen;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeGenerationSettingsBuilder;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.Optional;

public record AddFeaturesByFilterBiomeModifier(HolderSet<Biome> allowedBiomes, Optional<HolderSet<Biome>> deniedBiomes,
                                               Optional<Float> minimumTemperature, Optional<Float> maximumTemperature,
                                               HolderSet<PlacedFeature> features,
                                               GenerationStep.Decoration step) implements BiomeModifier{

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder){
        if(phase == Phase.ADD && this.allowedBiomes.contains(biome)){
            if(deniedBiomes.isPresent() && this.deniedBiomes.get().contains(biome)){
                return;
            }

            // PORT NOTE: Holder#get() was removed in favour of value().
            if(minimumTemperature.isPresent() && biome.value().getBaseTemperature() < minimumTemperature.get()){
                return;
            }

            if(maximumTemperature.isPresent() && biome.value().getBaseTemperature() > maximumTemperature.get()){
                return;
            }
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
        }
    }

    // PORT NOTE: BiomeModifier.codec() returns a MapCodec in NeoForge 1.21.
    @Override
    public MapCodec<? extends BiomeModifier> codec(){
        return LevelGen.ADD_FEATURES_BY_FILTER.get();
    }
}