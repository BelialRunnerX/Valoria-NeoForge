package com.idark.valoria.core.datagen;

import net.minecraft.core.registries.*;
import com.idark.valoria.*;
import com.idark.valoria.core.interfaces.DyeableItem;
import com.idark.valoria.registries.*;
import com.idark.valoria.registries.item.types.consumables.*;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.*;
import net.minecraft.data.tags.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.*;
import net.neoforged.neoforge.registries.*;
import org.jetbrains.annotations.*;

import java.util.concurrent.*;

public class ModItemTagsProvider extends ItemTagsProvider{

    public ModItemTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper){
        super(pOutput, pLookupProvider, pBlockTags, Valoria.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider){
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.FENCES, ItemTags.FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(BlockTags.DOORS, ItemTags.DOORS);
        copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
        copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
        copy(BlockTags.LOGS, ItemTags.LOGS);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.LEAVES, ItemTags.LEAVES);
        copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);

        // PORT NOTE: ItemTags.MUSIC_DISCS no longer exists in 1.21 (music discs are the JUKEBOX_PLAYABLE component), so the
        // necromancer disc is no longer tagged; its jukebox behaviour comes from Item.Properties#jukeboxPlayable.

        // PORT NOTE: FoodProperties.Builder#meat() is gone; wolves eat what is in minecraft:wolf_food. Same seven foods as 1.20.1.
        tag(ItemTags.WOLF_FOOD).add(
            ItemsRegistry.goblinMeat.get(), ItemsRegistry.cookedGoblinMeat.get(),
            ItemsRegistry.crabLeg.get(), ItemsRegistry.cookedCrablLeg.get(),
            ItemsRegistry.devilMeat.get(), ItemsRegistry.cookedDevilMeat.get(),
            ItemsRegistry.eyeChunk.get()
        );

        for(Item entry : BuiltInRegistries.ITEM){
            if(entry instanceof ValoriaFood) {
                tag(TagsRegistry.ROT_IMMUNE).add(entry);
            }

            if(!Valoria.ID.equals(BuiltInRegistries.ITEM.getKey(entry).getNamespace())) continue;

            // PORT NOTE: DyeableLeatherItem is gone; the cauldron/dye recipes look at minecraft:dyeable instead.
            if(entry instanceof DyeableItem){
                tag(ItemTags.DYEABLE).add(entry);
            }

            // PORT NOTE: EnchantmentCategory was removed in 1.21; enchantability is decided by the minecraft:enchantable/* item tags.
            // This reproduces the 1.20.1 category predicates for every Valoria item: WEAPON = SwordItem, DIGGER = DiggerItem,
            // sharpness on axes, ARMOR(+slot) = ArmorItem, WEARABLE = Equipable/Elytra, BOW/CROSSBOW/TRIDENT/FISHING_ROD by class,
            // BREAKABLE and VANISHABLE = any damageable item.
            if(entry instanceof SwordItem){
                tag(ItemTags.SWORD_ENCHANTABLE).add(entry);
            }
            if(entry instanceof DiggerItem){
                tag(ItemTags.MINING_ENCHANTABLE).add(entry);
                tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(entry);
            }
            if(entry instanceof AxeItem){
                tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(entry);
            }
            if(entry instanceof BowItem){
                tag(ItemTags.BOW_ENCHANTABLE).add(entry);
            }
            if(entry instanceof CrossbowItem){
                tag(ItemTags.CROSSBOW_ENCHANTABLE).add(entry);
            }
            if(entry instanceof TridentItem){
                tag(ItemTags.TRIDENT_ENCHANTABLE).add(entry);
            }
            if(entry instanceof FishingRodItem){
                tag(ItemTags.FISHING_ENCHANTABLE).add(entry);
            }
            if(entry instanceof ArmorItem armor){
                tag(ItemTags.ARMOR_ENCHANTABLE).add(entry);
                switch(armor.getType()){
                    case HELMET -> tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(entry);
                    case CHESTPLATE -> tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(entry);
                    case LEGGINGS -> tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(entry);
                    case BOOTS -> tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(entry);
                    default -> {}
                }
            }
            if(entry instanceof Equipable || entry instanceof ElytraItem){
                tag(ItemTags.EQUIPPABLE_ENCHANTABLE).add(entry);
            }
            if(entry.components().has(DataComponents.MAX_DAMAGE)){
                tag(ItemTags.DURABILITY_ENCHANTABLE).add(entry);
                tag(ItemTags.VANISHING_ENCHANTABLE).add(entry);
            }
        }
    }
}
