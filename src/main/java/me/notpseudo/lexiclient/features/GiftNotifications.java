package me.notpseudo.lexiclient.features;

import me.notpseudo.lexiclient.LexiClient;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.events.ClientChatEvent;
import me.notpseudo.lexiclient.utils.ChatUtils;
import me.notpseudo.lexiclient.utils.SoundUtils;
import me.notpseudo.lexiclient.utils.TextUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.notpseudo.lexiclient.LexiClient.mc;

public class GiftNotifications {

    private static final String ITEMS_STASHED_REGEX = "You have (\\d+) items stashed away!";
    private static final Pattern ITEMS_STASHED_PATTERN = Pattern.compile(ITEMS_STASHED_REGEX);
    private static final String GIFT_REGEX = "! (.+) §egift with §.(?:\\[.*?\\] )?[a-zA-Z0-9_]+§e!";
    private static final Pattern GIFT_PATTERN = Pattern.compile(GIFT_REGEX);

    private static int caresSinceLastClear = 0;
    private static boolean openedStash = true;

    private static boolean guiOpenEventReceived = false;
    private static int tickCount = 0;

    private static Set<String> caredItems = new HashSet<>();
    private static Map<String, Integer> foundGifts = new HashMap<>();

    private static long lastCareDropTime = 0L;
    private static String lastCareDrop = null;
    private static int itemsInStash = 0;

    private static final File CONFIG_FILE = new File(mc.mcDataDir, "config/lexiclient/goodgifts.txt");

    public static boolean loadGoodGifts() {
        if (!CONFIG_FILE.exists()) {
            resetGoodGifts();
        }
        try (Scanner reader = new Scanner(CONFIG_FILE).useDelimiter("\\Z");) {
            String content = reader.next().trim().replaceAll("\n", "");
            reader.close();
            caredItems.clear();
            Arrays.asList(content.split("[,\\n]")).forEach(s -> caredItems.add(s.trim()));
            return true;
        } catch (FileNotFoundException e) {
            LexiClient.LOGGER.error("Could not find config/lexiclient/goodgifts.txt", e);
            return false;
        }
    }

    public static void resetGoodGifts() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE, false)) {
            writer.write("Holly Dye,Snowman,Cryopowder Shard,Winter Island,Krampus Helmet,Golden Gift");
            writer.close();
        } catch (IOException e) {
            LexiClient.LOGGER.error("Could not write to config/lexiclient/goodgifts.txt", e);
        }
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String message = event.getMessage();
        if (message.startsWith("/viewstash")) {
            openedStash = true;
        } else if (message.startsWith("/clearstash")) {
            caresSinceLastClear = 0;
            foundGifts.clear();
        }
    }

    @SubscribeEvent
    public void onOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) guiOpenEventReceived = true;
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (event.type != 0) return;
        String stripped = TextUtils.stripColor(event.message.getUnformattedText());
        if (ChatUtils.isChatMessage(stripped)) return;
        if (LexiConfig.itemStashHud.isEnabled()) {
            Matcher itemMatcher = ITEMS_STASHED_PATTERN.matcher(stripped);
            if (itemMatcher.find()) {
                String count = itemMatcher.group(1);
                try {
                    itemsInStash = Integer.parseInt(count);
                    if (itemsInStash >= LexiConfig.itemStashLimit) {
                        openedStash = false;
                    }
                    return;
                } catch (NumberFormatException e) {
                    LexiClient.LOGGER.error("Failed to parse gift count: " + count);
                }
            }
        }
        if (LexiConfig.giftHud.isEnabled()) {
            String formatted = event.message.getFormattedText();
            Matcher giftMatcher = GIFT_PATTERN.matcher(formatted);
            if (giftMatcher.find()) {
                String gift = giftMatcher.group(1);
                String giftNoColor = TextUtils.stripColor(gift);
                if (caredItems.contains(giftNoColor)) {
                    foundGifts.put(gift, foundGifts.getOrDefault(gift, 0) + 1);
                    caresSinceLastClear++;
                    lastCareDropTime = System.currentTimeMillis();
                }
            }
        }
    }

    private static final String[] sounds = {"random.anvil_land", "mob.blaze.hit", "fire.ignite", "random.orb", "random.break", "mob.guardian.land.hit", "note.pling"};

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!LexiConfig.itemStashHud.isEnabled()) return;
        if (openedStash) return;
        if (guiOpenEventReceived) {
            if (mc.thePlayer != null) {
                if (mc.thePlayer.openContainer != null) guiOpenEventReceived = false;
                if (mc.thePlayer.openContainer instanceof ContainerChest) {
                    ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                    if (chest.getLowerChestInventory().getDisplayName().getUnformattedText().equals("View Stash")) {
                        openedStash = true;
                        return;
                    }
                }
            }
        }
        if (!LexiConfig.itemStashPlaySound) return;
        if (tickCount++ >= 10) {
            tickCount = 0;
            SoundUtils.playSound(sounds[LexiConfig.itemStashSound], LexiConfig.itemStashVolume, 1f);
        }
    }

    public static List<String> getHudStrings() {
        if (!LexiConfig.giftHud.isEnabled()) return Collections.emptyList();
        List<String> hudStrings = new ArrayList<>();
        if (System.currentTimeMillis() - lastCareDropTime < (LexiConfig.giftDisplayTime * 1000L) && lastCareDrop != null) {
            hudStrings.add(lastCareDrop);
        }
        if (LexiConfig.showSinceClear) hudStrings.add("Good Items Since Last Clear: " + caresSinceLastClear);
        if (LexiConfig.showSeparateGiftDrops) {
            foundGifts.forEach((k, v) -> hudStrings.add(k + ": " + v));
        }
        return hudStrings;
    }

    public static String getItemStashNotification() {
        if (!LexiConfig.itemStashHud.isEnabled() || openedStash) return "";
        return ChatUtils.ChatColor.RED + "Item Stash Almost Full!" + (LexiConfig.itemStashCount ? itemsInStash : "");
    }


}
