package com.idark.valoria.core.compat.kubejs;

import com.idark.valoria.*;
import com.idark.valoria.registries.item.recipe.*;
import com.mojang.datafixers.util.*;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.rhino.type.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

/**
 * PORT NOTE (upstream bug fix, continued): KubeJS recipe components that read the JSON layouts Valoria's own serializers
 * accept. KubeJS' stock {@code item_stack} component uses vanilla's {@code {"id","count"}} codec only, so with the plugin
 * finally loaded every existing Valoria recipe written in the 1.20.1 {@code {"item","count"}} form produced a
 * "Failed to parse recipe ... No key id" warning and fell back to vanilla parsing (scripts could not modify them).
 * {@link #ITEM_STACK} shares {@link RecipeCodecs#ITEM_STACK} (both forms) and {@link #COUNTED_INGREDIENT} reads the
 * {@code {"ingredient": ..., "count": n}} entries of heavy-workbench and alchemy recipes.
 */
public final class ValoriaRecipeComponents{
    private ValoriaRecipeComponents(){}

    public static final RecipeComponentType<ItemStack> ITEM_STACK = RecipeComponentType.unit(Valoria.loc("item_stack"),
        type -> new ItemStackComponent(type, RecipeCodecs.ITEM_STACK, false, Ingredient.EMPTY));

    public static final RecipeComponentType<Pair<Ingredient, RecipeData>> COUNTED_INGREDIENT = RecipeComponentType.unit(Valoria.loc("counted_ingredient"),
        CountedIngredientComponent::new);
}
