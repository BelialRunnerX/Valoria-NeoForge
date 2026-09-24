package com.idark.valoria.core.network.packets;

import com.idark.valoria.Valoria;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.neoforged.neoforge.network.handling.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.item.crafting.*;
import com.idark.valoria.core.compat.jei.categories.*;
import com.idark.valoria.registries.item.recipe.*;
import net.minecraft.network.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;
import java.util.function.*;

public class CrusherSyncPacket implements CustomPacketPayload{ // PORT NOTE: SimpleChannel message -> CustomPacketPayload
    public static final CustomPacketPayload.Type<CrusherSyncPacket> TYPE = new CustomPacketPayload.Type<>(Valoria.loc("crusher_sync_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrusherSyncPacket> STREAM_CODEC = StreamCodec.of((buf, msg) -> CrusherSyncPacket.encode(msg, buf), CrusherSyncPacket::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
    public ResourceLocation recipeId;
    public List<ItemStack> items;
    public CrusherSyncPacket(ResourceLocation recipeId, List<ItemStack> items){
        this.recipeId = recipeId;
        this.items = items;
    }

    public CrusherSyncPacket(RecipeHolder<CrusherRecipe> recipe, List<ItemStack> items){ // PORT NOTE: recipe ids live on the RecipeHolder in 1.21
        this(recipe.id(), items);
    }

    // PORT NOTE: FriendlyByteBuf#writeItem/readItem are gone; item stacks go through ItemStack's stream codecs on a RegistryFriendlyByteBuf.
    public static void encode(CrusherSyncPacket object, RegistryFriendlyByteBuf buffer){
        buffer.writeResourceLocation(object.recipeId);
        ItemStack.OPTIONAL_LIST_STREAM_CODEC.encode(buffer, object.items);
    }

    public static CrusherSyncPacket decode(RegistryFriendlyByteBuf buffer){
        return new CrusherSyncPacket(buffer.readResourceLocation(), new ArrayList<>(ItemStack.OPTIONAL_LIST_STREAM_CODEC.decode(buffer)));
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            CrusherRecipeCategory.DROPS.put(recipeId, items);
        });

    }
}
