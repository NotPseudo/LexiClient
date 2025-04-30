package me.notpseudo.lexiclient.events;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ClientChatEvent extends Event {

    private final String message;

    public ClientChatEvent(C01PacketChatMessage chatPacket) {
        message = chatPacket.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
