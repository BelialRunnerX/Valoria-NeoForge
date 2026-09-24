package com.idark.valoria.core.capability;

import com.idark.valoria.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.registries.*;

import java.util.function.*;

/**
 * PORT NOTE: Forge capabilities attached through {@code AttachCapabilitiesEvent<Entity>} became NeoForge data attachments.
 * The four player capabilities keep their 1.20.1 ids ({@code valoria:pages}, {@code valoria:nihility_level},
 * {@code valoria:magma_level}, {@code valoria:ability_tracker}) and NBT layouts, so saved data is read back unchanged.
 * All four are {@code copyOnDeath}: the old {@code PlayerEvent.Clone} handler copied pages/nihility/magma on every
 * clone and abilities only on death; NeoForge copies every attachment on non-death clones itself, so the only
 * behavioural difference is that ability cooldowns now also survive an End-portal return.
 */
public final class ValoriaAttachments{
    private ValoriaAttachments(){}

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Valoria.ID);

    public static final Supplier<AttachmentType<UnlockableProvider>> UNLOCKABLES = ATTACHMENTS.register("pages", () -> AttachmentType.serializable(UnlockableProvider::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<NihilityLevelProvider>> NIHILITY = ATTACHMENTS.register("nihility_level", () -> AttachmentType.serializable(NihilityLevelProvider::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<MagmaLevelProvider>> MAGMA = ATTACHMENTS.register("magma_level", () -> AttachmentType.serializable(MagmaLevelProvider::new).copyOnDeath().build());
    public static final Supplier<AttachmentType<PlayerAbilityTracker>> ABILITIES = ATTACHMENTS.register("ability_tracker", () -> AttachmentType.serializable(PlayerAbilityTracker::new).copyOnDeath().build());

    public static void register(IEventBus eventBus){
        ATTACHMENTS.register(eventBus);
    }
}
