package com.idark.valoria.core.network.packets;

import com.idark.valoria.Valoria;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.neoforged.neoforge.network.handling.*;
import com.idark.valoria.core.capability.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.*;

import javax.annotation.*;
import java.util.function.*;

public class NihilityPacket implements CustomPacketPayload{ // PORT NOTE: SimpleChannel message -> CustomPacketPayload
    public static final CustomPacketPayload.Type<NihilityPacket> TYPE = new CustomPacketPayload.Type<>(Valoria.loc("nihility_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NihilityPacket> STREAM_CODEC = StreamCodec.of((buf, msg) -> NihilityPacket.encode(msg, buf), NihilityPacket::decode);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
    private final float max;
    private final float nihilityLevel;

    public NihilityPacket(float max, float amount){
        this.max = max;
        this.nihilityLevel = amount;
    }

    public NihilityPacket(INihilityLevel nihility, @Nullable LivingEntity entity) {
        this.max = nihility.getMaxAmount(entity);
        this.nihilityLevel = nihility.getAmount();
    }

    public static void encode(NihilityPacket object, FriendlyByteBuf buffer){
        buffer.writeFloat(object.max);
        buffer.writeFloat(object.nihilityLevel);
    }

    public static NihilityPacket decode(FriendlyByteBuf buffer){
        return new NihilityPacket(buffer.readFloat(), buffer.readFloat());
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            // PORT NOTE: client player fetched through the sided proxy - referencing LocalPlayer here made the JVM verifier load the
            // client-only class when the payload classes were registered on a dedicated server.
            net.minecraft.world.entity.player.Player player = Valoria.proxy.getPlayer();
            if(player == null) return;
            INihilityLevel.of(player).ifPresent(nihility -> {
                nihility.setMaxAmount(this.max);
                nihility.setAmount(this.nihilityLevel);
            });
        });

    }
}