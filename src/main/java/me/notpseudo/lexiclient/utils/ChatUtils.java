package me.notpseudo.lexiclient.utils;

import me.notpseudo.lexiclient.config.LexiConfig;
import net.minecraft.util.ChatComponentText;

import java.util.regex.Pattern;

import static me.notpseudo.lexiclient.LexiClient.mc;

/**
 * Utility class to send messages to or from the player
 *
 * Credit to DulkirMod
 */
public class ChatUtils {

    public static void sendToClient(String text, boolean prefix) {
        if (mc.thePlayer == null) return;
        String textPrefix = prefix ? LexiConfig.MOD_PREFIX + " " : "";
        mc.thePlayer.addChatMessage(new ChatComponentText(textPrefix + text + "§r"));
    }

    public static void success(String text) {
        sendToClient(ChatColor.GREEN + text, true);
    }

    public static void error(String text) {
        sendToClient(ChatColor.RED + text, true);
    }

    public static void info(String text) {
        sendToClient(ChatColor.WHITE + text, true);
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

    private static final String ALL_CHAT_REGEX = "^(?:\\[.*?\\] )?([a-zA-Z0-9_]+)(?: \\[.*?\\])?: (.+)$";
    private static final Pattern ALL_CHAT_PATTERN = Pattern.compile(ALL_CHAT_REGEX);
    private static final String PARTY_CHAT_REGEX = "^Party > (?:\\[.*?\\] )?([a-zA-Z0-9_]+): (.+)$";
    private static final Pattern PARTY_CHAT_PATTERN = Pattern.compile(PARTY_CHAT_REGEX);
    private static final String GUILD_CHAT_REGEX = "^Guild > (?:\\[.*?\\] )?([a-zA-Z0-9_]+)(?: \\[.*?\\])?: (.+)$";
    private static final Pattern GUILD_CHAT_PATTERN = Pattern.compile(GUILD_CHAT_REGEX);
    private static final String OFFICER_CHAT_REGEX = "^Officer > (?:\\[.*?\\] )?([a-zA-Z0-9_]+)(?: \\[.*?\\])?: (.+)$";
    private static final Pattern OFFICER_CHAT_PATTERN = Pattern.compile(OFFICER_CHAT_REGEX);
    private static final String COOP_CHAT_REGEX = "^Co-op > (?:\\[.*?\\] )?([a-zA-Z0-9_]+)(?: \\[.*?\\])?: (.+)$";
    private static final Pattern COOP_CHAT_PATTERN = Pattern.compile(COOP_CHAT_REGEX);

    public static boolean isAllChatMessage(String message) {
        return ALL_CHAT_PATTERN.matcher(message).matches();
    }

    public static boolean isPartyChatMessage(String message) {
        return PARTY_CHAT_PATTERN.matcher(message).matches();
    }

    public static boolean isGuildChatMessage(String message) {
        return GUILD_CHAT_PATTERN.matcher(message).matches();
    }

    public static boolean isOfficerChatMessage(String message) {
        return OFFICER_CHAT_PATTERN.matcher(message).matches();
    }

    public static boolean isCoopChatMessage(String message) {
        return COOP_CHAT_PATTERN.matcher(message).matches();
    }

    public static boolean isChatMessage(String message) {
        return isAllChatMessage(message) || isPartyChatMessage(message) || isGuildChatMessage(message) || isOfficerChatMessage(message) || isCoopChatMessage(message);
    }

    public enum ChatColor {
        BLACK(0) ,
        DARK_BLUE(1),
        DARK_GREEN(2),
        DARK_AQUA(3),
        DARK_RED(4),
        DARK_PURPLE(5),
        GOLD(6),
        GRAY(7),
        DARK_GRAY(8),
        BLUE(9),
        GREEN('a'),
        AQUA('b'),
        RED('c'),
        LIGHT_PURPLE('d'),
        YELLOW('e'),
        WHITE('f'),
        OBFUSCATED('k'),
        BOLD('l'),
        STRIKETHROUGH('m'),
        UNDERLINE('n'),
        ITALIC('o'),
        RESET('r');
        private final String code;

        private ChatColor(int code) {
            this.code = "§" + code;
        }
        private ChatColor(char code) {
            this.code = "§" + code;
        }

        @Override
        public String toString() {
            return code;
        }
    }

}
