package com.idark.valoria.core.mixin.client;

import com.idark.valoria.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.*;
import net.minecraft.client.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

/**
 * PORT NOTE: AbstractClientPlayer#getCloakTextureLocation() no longer exists in 1.21 - cape and elytra textures are
 * carried by the {@link PlayerSkin} record returned from {@link AbstractClientPlayer#getSkin()}. The supporter cloak
 * is therefore injected by returning a copy of the skin whose cape texture is swapped. Leaving elytraTexture untouched
 * keeps the 1.20.1 behaviour: ElytraLayer falls back to the cape texture when the player has no dedicated elytra skin.
 */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow
    protected abstract PlayerInfo getPlayerInfo();

    @Inject(method = "getSkin", at = @At(value = "RETURN"), cancellable = true)
    private void valoria$getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerInfo playerInfo = this.getPlayerInfo();
        // PORT NOTE: GameProfile#isComplete() is gone in authlib 6; a profile with a name is what the cloak lookup needs.
        if (playerInfo == null || playerInfo.getProfile().getName() == null || playerInfo.getProfile().getName().isEmpty()) return;

        String playerName = playerInfo.getProfile().getName();
        PlayerSkin skin = cir.getReturnValue();
        if (skin == null) return;
        for(Cloaks cape : Cloaks.values()) {
            if(playerName.equals(cape.name)) {
                cir.setReturnValue(new PlayerSkin(skin.texture(), skin.textureUrl(), cape.texture, skin.elytraTexture(), skin.model(), skin.secure()));
            }
        }
    }
}
