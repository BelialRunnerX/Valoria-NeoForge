package com.idark.valoria.core.command.arguments;

import net.minecraft.core.registries.*;
import com.idark.valoria.Valoria;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModArgumentTypes{
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARG_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Valoria.ID);
    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> UNLOCKABLE_ARG = ARG_TYPES.register("unlockable", () -> ArgumentTypeInfos.registerByClass(UnlockableArgumentType.class, SingletonArgumentInfo.contextFree(UnlockableArgumentType::unlockableArgumentType)));

    public static void register(IEventBus eventBus){
        ARG_TYPES.register(eventBus);
    }
}
