package com.idark.valoria.core.compat.kubejs;

import com.idark.valoria.*;
import com.idark.valoria.core.compat.kubejs.schemas.*;
import dev.latvian.mods.kubejs.plugin.*;
import dev.latvian.mods.kubejs.recipe.schema.*;

/**
 * PORT NOTE: KubeJS 2101 (1.21.1): KubeJSPlugin is an interface, schemas are registered through RecipeSchemaRegistry by
 * ResourceLocation, and the built-in CookingRecipeSchema class is gone (vanilla schemas are data-driven), so the kiln
 * schema is declared explicitly in {@link KilnRecipeSchema} with the same keys.
 */
public class ValoriaKJSPlugin implements KubeJSPlugin{
    @Override
    public void init() {
    }

    // PORT NOTE (upstream bug fix): the plugin is discovered through src/main/resources/kubejs.plugins.txt, which upstream
    // never shipped - on 1.20.1 this class was dead code and event.recipes.valoria.* did not exist.
    @Override
    public void registerRecipeComponents(dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry registry) {
        registry.register(ValoriaRecipeComponents.ITEM_STACK);
        registry.register(ValoriaRecipeComponents.COUNTED_INGREDIENT);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(Valoria.loc("kiln"), KilnRecipeSchema.SCHEMA);
        registry.register(Valoria.loc("crusher"), CrusherRecipeSchema.SCHEMA);
        registry.register(Valoria.loc("jewelry"), JewelryRecipeSchema.SCHEMA);
        registry.register(Valoria.loc("keg_brewery"), KegRecipeSchema.SCHEMA);
        registry.register(Valoria.loc("heavy_workbench"), HeavyWorkbenchRecipeSchema.SCHEMA);
        registry.register(Valoria.loc("manipulator"), ManipulatorRecipeSchema.SCHEMA);
    }
}
