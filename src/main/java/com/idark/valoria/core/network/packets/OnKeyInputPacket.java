package com.idark.valoria.core.network.packets;

import com.idark.valoria.Valoria;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.neoforged.neoforge.network.handling.*;
import net.minecraft.core.registries.*;
import com.idark.valoria.core.network.*;
import com.idark.valoria.registries.item.types.*;
import net.minecraft.network.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import top.theillusivec4.curios.api.*;

public class OnKeyInputPacket extends RateLimitedPacket{
    public static final CustomPacketPayload.Type<OnKeyInputPacket> TYPE = new CustomPacketPayload.Type<>(Valoria.loc("on_key_input_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OnKeyInputPacket> STREAM_CODEC = StreamCodec.of((buf, msg) -> OnKeyInputPacket.encode(msg, buf), OnKeyInputPacket::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
    private final int event;
    public OnKeyInputPacket(int event){
        this.event = event;
    }

    public static void encode(OnKeyInputPacket msg, FriendlyByteBuf buffer){
        buffer.writeVarInt(msg.event);
    }

    public static OnKeyInputPacket decode(FriendlyByteBuf buffer){
        return new OnKeyInputPacket(buffer.readVarInt());
    }

    public void execute(ServerPlayer player){
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            handler.getCurios().forEach((id, stackHandler) -> {
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack serverStack = stackHandler.getStacks().getStackInSlot(i);
                    if (!serverStack.isEmpty() && serverStack.getItem() instanceof InputListener listener) {
                        listener.onInput(player, serverStack, this.event);
                    }
                }
            });
        });
    }
}