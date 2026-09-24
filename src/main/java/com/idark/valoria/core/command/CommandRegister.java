package com.idark.valoria.core.command;

import com.idark.valoria.Valoria;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Valoria.ID, bus = EventBusSubscriber.Bus.GAME)
public class CommandRegister{

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent e){
        ModCommand.register(e.getDispatcher());
    }
}
