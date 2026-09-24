package com.idark.valoria.core.network.packets;

import com.idark.valoria.Valoria;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.neoforged.neoforge.network.handling.*;
import com.idark.valoria.core.capability.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.*;

import javax.annotation.*;
import java.util.function.*;

public class MagmaPacket implements CustomPacketPayload{ // PORT NOTE: SimpleChannel message -> CustomPacketPayload
    public static final CustomPacketPayload.Type<MagmaPacket> TYPE = new CustomPacketPayload.Type<>(Valoria.loc("magma_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MagmaPacket> STREAM_CODEC = StreamCodec.of((buf, msg) -> MagmaPacket.encode(msg, buf), MagmaPacket::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
    private final float max;
    private final float magmaLevel;

    public MagmaPacket(float max, float amount){
        this.max = max;
        this.magmaLevel = amount;
    }

    public MagmaPacket(IMagmaLevel magma, @Nullable LivingEntity entity) {
        this.max = magma.getMaxAmount(entity);
        this.magmaLevel = magma.getAmount();
    }

    public static void encode(MagmaPacket object, FriendlyByteBuf buffer){
        buffer.writeFloat(object.max);
        buffer.writeFloat(object.magmaLevel);
    }

    public static MagmaPacket decode(FriendlyByteBuf buffer){
        return new MagmaPacket(buffer.readFloat(), buffer.readFloat());
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if(player == null) return;
            IMagmaLevel.of(player).ifPresent(m -> {
                m.setMaxAmount(this.max);
                m.setAmount(this.magmaLevel);
            });
        });

    }
}