package com.idark.valoria.client.event;

import com.idark.valoria.ValoriaClient;
import com.idark.valoria.client.ui.screen.JewelryBagScreen;
import com.idark.valoria.registries.item.types.curio.JewelryBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.InputEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class KeyBindHandler{
    private KeyBindHandler(){
    }

    /**
     * PORT NOTE: 1.20.1 registered {@code forgeBus.addListener(KeyBindHandler::onInput)} for the abstract {@code InputEvent},
     * which Forge delivered for every subclass. NeoForge's bus rejects abstract event types, so the same handler is
     * subscribed to each concrete InputEvent once (Key, MouseButton.Pre/Post, MouseScrollingEvent,
     * InteractionKeyMappingTriggered) - identical coverage. The inert @SubscribeEvent annotation was dropped.
     */
    public static void register(IEventBus bus){
        bus.addListener(EventPriority.NORMAL, false, InputEvent.Key.class, KeyBindHandler::onInput);
        bus.addListener(EventPriority.NORMAL, false, InputEvent.MouseButton.Pre.class, KeyBindHandler::onInput);
        bus.addListener(EventPriority.NORMAL, false, InputEvent.MouseButton.Post.class, KeyBindHandler::onInput);
        bus.addListener(EventPriority.NORMAL, false, InputEvent.MouseScrollingEvent.class, KeyBindHandler::onInput);
        bus.addListener(EventPriority.NORMAL, false, InputEvent.InteractionKeyMappingTriggered.class, KeyBindHandler::onInput);
    }

    public static void onInput(InputEvent event){
        if(ValoriaClient.BAG_MENU_KEY.isDown()){
            jewelryBagMenu();
        }
    }

    @SuppressWarnings("removal")
    public static void jewelryBagMenu(){
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        List<ItemStack> items = new ArrayList<>();
        List<SlotResult> curioSlots = CuriosApi.getCuriosHelper().findCurios(player, (i) -> true);
        for(SlotResult slot : curioSlots){
            if(slot.stack().getItem() instanceof JewelryBagItem){
                items.add(slot.stack());
            }
        }

        if(!items.isEmpty()){
            mc.setScreen(new JewelryBagScreen(Component.empty()));
        }
    }
}
