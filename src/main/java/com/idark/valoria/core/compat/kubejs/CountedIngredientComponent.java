package com.idark.valoria.core.compat.kubejs;

import com.idark.valoria.registries.item.recipe.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import dev.latvian.mods.kubejs.recipe.*;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.rhino.type.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

/**
 * PORT NOTE (upstream bug fix, continued): KubeJS component for the {@code {"ingredient": ..., "count": n}} entries of
 * heavy-workbench (and alchemy) recipes. JSON goes through {@link RecipeCodecs#COUNTED_INGREDIENT}; scripts may pass
 * {@code {ingredient: 'mod:item', count: 2}} or a plain ingredient (count 1).
 */
public record CountedIngredientComponent(RecipeComponentType<?> type) implements RecipeComponent<Pair<Ingredient, RecipeData>>{
    @Override
    public Codec<Pair<Ingredient, RecipeData>> codec(){
        return RecipeCodecs.COUNTED_INGREDIENT;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public TypeInfo typeInfo(){
        return (TypeInfo) TypeInfo.of(Pair.class);
    }

    @Override
    public Pair<Ingredient, RecipeData> wrap(RecipeScriptContext cx, Object from){
        if(from instanceof Pair<?, ?> pair && pair.getFirst() instanceof Ingredient ing && pair.getSecond() instanceof RecipeData data){
            return Pair.of(ing, data);
        }

        RecipeComponent<Ingredient> ingredientComponent = IngredientComponent.INGREDIENT.instance();
        if(from instanceof Map<?, ?> map && map.containsKey("ingredient")){
            Ingredient ingredient = ingredientComponent.wrap(cx, map.get("ingredient"));
            int count = map.get("count") instanceof Number n ? n.intValue() : 1;
            return Pair.of(ingredient, new RecipeData(count));
        }

        return Pair.of(ingredientComponent.wrap(cx, from), new RecipeData(1));
    }

    @Override
    public boolean isEmpty(Pair<Ingredient, RecipeData> value){
        return value.getFirst().isEmpty();
    }

    @Override
    public String toString(){
        return "valoria:counted_ingredient";
    }
}
