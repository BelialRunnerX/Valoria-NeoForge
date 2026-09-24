// PORT NOTE (port metadata - dev tooling): companion to porttest.js. Adds recipes through the KubeJS schemas the port
// registers (ValoriaKJSPlugin, discovered via kubejs.plugins.txt) so the harness can prove the schemas produce recipes
// that the blocks actually execute. Copy next to porttest.js in <run dir>/kubejs/server_scripts/.
//
// Script API (argument order = schema key order; keys marked ? are optional and can also be set with .key(value)):
//   valoria.kiln(result, ingredient, experience?, cookingtime?)
//   valoria.jewelry(output, ingredients[], time)
//   valoria.keg_brewery(output, ingredients[], time)
//   valoria.crusher(loot_table, ingredients[])
//   valoria.heavy_workbench(result, [{ingredient, count}], group)
//   valoria.manipulator(output, core, cores, time, ingredients[])
ServerEvents.recipes(event => {
  event.recipes.valoria.kiln('minecraft:diamond', 'minecraft:dirt').cookingtime(60).experience(0.5).id('porttest:kiln_dirt_to_diamond')
  event.recipes.valoria.jewelry('minecraft:emerald', ['valoria:empty_gazer', 'minecraft:dirt'], 60).id('porttest:jewelry_dirt_to_emerald')
  event.recipes.valoria.crusher('valoria:items/gem_crashing', ['minecraft:dirt']).id('porttest:crusher_dirt')
  event.recipes.valoria.keg_brewery('minecraft:apple', ['minecraft:dirt', 'valoria:bottle'], 60).id('porttest:keg_dirt_to_apple')
  event.recipes.valoria.heavy_workbench('minecraft:stick', [{ ingredient: 'minecraft:dirt', count: 2 }], 'equipment').id('porttest:workbench_dirt_to_stick')
  event.recipes.valoria.manipulator('minecraft:gold_ingot', 'empty', 0, 20, ['minecraft:dirt', 'minecraft:sand']).id('porttest:manipulator_dirt_to_gold')
})
