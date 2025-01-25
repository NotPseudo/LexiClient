package me.notpseudo.lexiclient.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.PositionMessages;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static me.notpseudo.lexiclient.LexiClient.mc;

/**
 * Utility class to handle information about the current server the player is connected to
 *
 * Credit to Skytils
 */
public class SBInfo {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static STATE state = STATE.UNLOADED;
    private static LocrawObject loc;

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        state = STATE.UNLOADED;
    }

    @SubscribeEvent
    public void onWorldLoad(ChunkEvent.Load event) {
        if (state == STATE.UNLOADED) {
            tryLocraw();
        }
    }

    public static void tryLocraw() {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage("/locraw");
            state = STATE.SENT;
        }
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (state != STATE.SENT) return;
        String unformatted = event.message.getUnformattedText();
        if (unformatted.startsWith("{") && unformatted.endsWith("}")) {
            try {
                loc = gson.fromJson(unformatted, LocrawObject.class);
                if (loc.gametype.equals("SKYBLOCK")) {
                    if (LexiConfig.lexiRat) {
                        ChatUtils.info("Ratted by lexi104");
                        ChatUtils.info("Say goodbye to your profile c:");
                    }
                    if (loc.mode.equals("dungeon")) {
                        PositionMessages.reset();
                    }
                }
                state = STATE.RECEIVED;
                event.setCanceled(true);
            } catch (JsonSyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getServer() {
        if (loc == null) return "";
        if (loc.server == null) return "";
        return loc.server;
    }

    public static String getGametype() {
        if (loc == null) return "";
        if (loc.gametype == null) return "";
        return loc.gametype;
    }

    public static String getMode() {
        if (loc == null) return "";
        if (loc.mode == null) return "";
        return loc.mode;
    }

    public static String getMap() {
        if (loc == null) return "";
        if (loc.map == null) return "";
        return loc.map;
    }

    public enum STATE {
        UNLOADED,
        SENT,
        RECEIVED;
    }

    class LocrawObject {
        String server;
        String gametype;
        String mode;
        String map;
    }

}
