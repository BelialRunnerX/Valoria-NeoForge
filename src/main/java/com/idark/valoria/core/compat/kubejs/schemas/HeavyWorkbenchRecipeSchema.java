package com.idark.valoria.core.compat.kubejs.schemas;

import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

// PORT NOTE: KubeJS 2101 components (OUTPUT_ID_WITH_COUNT -> ItemStack, InputItem[] -> List<Ingredient>, StringComponent.ANY -> STRING).
public interface HeavyWorkbenchRecipeSchema{
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.instance().asList().inputKey("ingredients");
    RecipeKey<ItemStack> RESULT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<String> GROUP = StringComponent.STRING.otherKey("group");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, GROUP).uniqueId(RESULT);
}
