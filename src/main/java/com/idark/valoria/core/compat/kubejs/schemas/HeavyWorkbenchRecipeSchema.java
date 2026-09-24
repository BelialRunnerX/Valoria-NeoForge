package com.idark.valoria.core.compat.kubejs.schemas;

import com.idark.valoria.core.compat.kubejs.*;
import com.idark.valoria.registries.item.recipe.*;
import com.mojang.datafixers.util.*;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

// PORT NOTE: KubeJS 2101 components (OUTPUT_ID_WITH_COUNT -> ItemStack, InputItem[] -> List<Ingredient>, StringComponent.ANY -> STRING).
// The result uses Valoria's lenient item-stack component and the ingredients the {"ingredient","count"} entries the
// serializer reads, so existing JSON parses without warnings and scripts pass {ingredient: 'mod:item', count: n}.
public interface HeavyWorkbenchRecipeSchema{
    RecipeKey<List<Pair<Ingredient, RecipeData>>> INGREDIENTS = ValoriaRecipeComponents.COUNTED_INGREDIENT.instance().asList().inputKey("ingredients");
    RecipeKey<ItemStack> RESULT = ValoriaRecipeComponents.ITEM_STACK.instance().outputKey("result");
    RecipeKey<String> GROUP = StringComponent.STRING.otherKey("group");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, GROUP).uniqueId(RESULT);
}
