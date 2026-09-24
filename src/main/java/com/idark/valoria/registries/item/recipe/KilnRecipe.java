package com.idark.valoria.registries.item.recipe;

import com.idark.valoria.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

/**
 * PORT NOTE: AbstractCookingRecipe lost its id and gained the standard {@code Factory} constructor shape in 1.21, so
 * the kiln recipe now uses vanilla's {@link SimpleCookingSerializer} (JSON keys {@code ingredient}, {@code result},
 * {@code experience}, {@code cookingtime} - default 120 - exactly as before; the result is written as
 * {@code {"id": ...}} like every other 1.21 cooking recipe).
 */
public class KilnRecipe extends AbstractCookingRecipe{
    // PORT NOTE: recipe ids live on the RecipeHolder in 1.21; stamped onto the instance for JEI's registry name (see ModJeiRecipes).
    private ResourceLocation id;

    public ResourceLocation getId(){
        return id;
    }

    public KilnRecipe withId(ResourceLocation id){
        this.id = id;
        return this;
    }

    public KilnRecipe(String group, CookingBookCategory category, Ingredient pIngredient, ItemStack pResult, float pExperience, int cookingTime){
        super(Type.INSTANCE, group, category, pIngredient, pResult, pExperience, cookingTime);
    }

    public KilnRecipe(Ingredient pIngredient, ItemStack pResult, float pExperience, int cookingTime){
        this("kiln", CookingBookCategory.MISC, pIngredient, pResult, pExperience, cookingTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer(){
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType(){
        return Type.INSTANCE;
    }

    @Override
    public boolean isSpecial(){
        return true;
    }

    public static class Type implements RecipeType<KilnRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "kiln";
    }

    public static class Serializer extends SimpleCookingSerializer<KilnRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = Valoria.loc("kiln");

        public Serializer(){
            super(KilnRecipe::new, 120);
        }
    }
}
