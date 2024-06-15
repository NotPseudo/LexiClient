package me.notpseudo.lexiclient.utils;

import net.minecraft.util.ChatComponentText;

import static me.notpseudo.lexiclient.LexiClient.CHAT_PREFIX;
import static me.notpseudo.lexiclient.LexiClient.mc;

/**
 * Utility class to send messages to or from the player
 *
 * Credit to DulkirMod
 */
public class ChatUtils {

    public static void info(String text, boolean prefix) {
        if (mc.thePlayer == null) return;
        String textPrefix = prefix ? CHAT_PREFIX : "";
        mc.thePlayer.addChatMessage(new ChatComponentText(textPrefix + " " + text + "§r"));
    }

    public static void info(String text) {
        info(text, true);
    }

    public static void toggledMessage(String message, boolean state) {
        String stateText = state ? "§aON" : "§cOFF";
        info("§9Toggled " + message + " §8[" + stateText + "§8]§r");
    }

    public static void sendPartyChatMessage(String message) {
        sendMessage("/pc " + message);
    }

    public static void sendMessage(String message) {
        mc.thePlayer.sendChatMessage(message);
    }

}
