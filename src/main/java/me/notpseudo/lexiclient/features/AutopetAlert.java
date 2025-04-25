package me.notpseudo.lexiclient.features;

import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.utils.TextUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutopetAlert {

    private static final String AUTOPET_REGEX = "(§.\\[Lvl \\d+]) (§.+)§e!";
    private static final Pattern AUTOPET_PATTERN = Pattern.compile(AUTOPET_REGEX);
    private static String notification = "";
    private static long lastNotification = 0L;

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (!LexiConfig.autopetHud.isEnabled()) return;
        if (event.type != 0) return;
        if (!TextUtils.stripColor(event.message.getUnformattedText()).startsWith("Autopet")) return;
        String formatted = event.message.getFormattedText();
        Matcher matcher = AUTOPET_PATTERN.matcher(formatted);
        if (matcher.find()) {
            String equipped = LexiConfig.autopetIncludeEquipped ? "Equipped " : "";
            String level = LexiConfig.autopetShowLevel ? matcher.group(1) + " " : "";
            String pet = matcher.group(2);
            notification = equipped + level + pet;
            lastNotification = System.currentTimeMillis();
        }
    }

    public static String getNotification() {
        if (System.currentTimeMillis() - lastNotification > (long) (LexiConfig.autopetTime * 1000L)) {
            return "";
        }
        return notification;
    }

}
