package com.idark.valoria.core.compat.kubejs.schemas;

import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

// PORT NOTE: KubeJS 2101 components (OutputItem -> ItemStack, InputItem[][] -> List<Ingredient>); uniqueOutputId -> uniqueId.
public interface ManipulatorRecipeSchema {
    RecipeKey<String> CORE = StringComponent.ID.otherKey("core");
    RecipeKey<Integer> CORES = NumberComponent.INT.otherKey("cores");
    RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time");

    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().inputKey("ingredients");
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("output");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, CORE, CORES, TIME, INGREDIENTS).uniqueId(OUTPUT);
}
