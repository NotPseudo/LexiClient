package me.notpseudo.lexiclient.mixin;

import io.netty.channel.ChannelHandlerContext;
import me.notpseudo.lexiclient.events.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class to inject into NetworkManager to handle receiving packets. Largely borrowed from odtheking/Odin
 */
@Mixin(value = {NetworkManager.class}, priority = 1001)
public class MixinNetworkManager {

    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent.Receive packetEvent = new PacketEvent.Receive(packet);
        MinecraftForge.EVENT_BUS.post(packetEvent);
        if (packetEvent.isCanceled() && callbackInfo.isCancellable()) callbackInfo.cancel();
    }

    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Send packetEvent = new PacketEvent.Send(packet);
        MinecraftForge.EVENT_BUS.post(packetEvent);
    }


}
