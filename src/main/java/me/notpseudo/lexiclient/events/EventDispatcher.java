package me.notpseudo.lexiclient.events;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventDispatcher {

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof S32PacketConfirmTransaction) {
            MinecraftForge.EVENT_BUS.post(new ServerTickEvent());
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            MinecraftForge.EVENT_BUS.post(new ClientChatEvent((C01PacketChatMessage) event.getPacket()));
        }
    }

}
