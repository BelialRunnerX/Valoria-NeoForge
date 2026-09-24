package com.idark.valoria.registries.item.armor.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.*;
import net.minecraft.core.registries.*;
import com.google.common.collect.*;
import com.idark.valoria.client.render.armor.*;
import com.idark.valoria.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.decoration.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import com.idark.valoria.Valoria;
import net.neoforged.neoforge.client.extensions.common.*;
import org.jetbrains.annotations.*;
import pro.komaru.tridot.client.model.armor.*;
import pro.komaru.tridot.common.registry.item.armor.*;
import software.bernie.geckolib.animatable.*;
import software.bernie.geckolib.constant.*;
import software.bernie.geckolib.animatable.instance.*;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.*;
import software.bernie.geckolib.util.*;

import java.util.*;
import java.util.function.*;

public class SamuraiArmorItem extends SuitArmorItem implements GeoItem{
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public SamuraiArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties){
        super(material, type, properties);
    }

    // prevents log spam
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel){
        return ResourceLocation.withDefaultNamespace("textures/models/armor/diamond_layer_1.png"); // PORT NOTE: getArmorTexture returns a ResourceLocation and receives the material layer in 1.21
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                ArmorModel model = getArmorModel(livingEntity, itemStack, equipmentSlot, original);
                if(model != null) return model;

                if (this.renderer == null) this.renderer = new SamuraiArmorRenderer();
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 20, state -> {
            state.setAnimation(DefaultAnimations.IDLE);
            Entity entity = state.getData(DataTickets.ENTITY);
            if (entity instanceof ArmorStand)
                return PlayState.CONTINUE;

            // PORT NOTE: getArmorSlots() moved from Entity to LivingEntity in 1.21.
            if (!(entity instanceof LivingEntity living))
                return PlayState.STOP;

            Set<Item> wornArmor = new ObjectOpenHashSet<>();
            for (ItemStack stack : living.getArmorSlots()) {
                if (stack.isEmpty())
                    return PlayState.STOP;

                wornArmor.add(stack.getItem());
            }

            boolean isFullSet = wornArmor.containsAll(ObjectArrayList.of(
            ItemsRegistry.samuraiBoots.get(),
            ItemsRegistry.samuraiLeggings.get(),
            ItemsRegistry.samuraiChestplate.get(),
            ItemsRegistry.samuraiKabuto.get()));

            return isFullSet ? PlayState.CONTINUE : PlayState.STOP;
        }));
    }

    public float getBonusValue(EquipmentSlot slot){
        return switch(slot){
            case CHEST -> 0.15f;
            case HEAD, FEET -> 0.05f;
            case LEGS -> 0.1f;
            default -> 0;
        };
    }

    // PORT NOTE: getDefaultAttributeModifiers(EquipmentSlot) -> ItemAttributeModifiers with slot groups; the piece's
    // dash-distance bonus is added for the slot the piece is worn in, exactly as before.
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(){
        EquipmentSlot slot = type.getSlot();
        return super.getDefaultAttributeModifiers().withModifierAdded(AttributeReg.DASH_DISTANCE, new AttributeModifier(Valoria.loc("samurai_dash_bonus"), getBonusValue(slot), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(slot));
    }
}