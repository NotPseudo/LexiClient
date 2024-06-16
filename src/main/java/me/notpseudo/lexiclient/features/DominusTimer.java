package me.notpseudo.lexiclient.features;

import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.utils.ArmorUtils;
import me.notpseudo.lexiclient.utils.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static me.notpseudo.lexiclient.LexiClient.mc;
import static me.notpseudo.lexiclient.core.CrimsonArmorAbilityStack.CRIMSON;

public class DominusTimer {

    private static int oldKill = -1;
    private static String oldID = "";
    public static int stack = 0;
    public static long lastStackUpdate = 0L;
    public static long stackLoseTime = 0L;
    public static boolean stopTimer = true;

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type != 2) return;
        if (!LexiConfig.dominusHud.isEnabled()) return;
        updateStack(event.message.getUnformattedText());
    }

    /**
     * Updates the number of stacks and restarts the timer if there is a change. If stacks reaches 0, stops the timer
     *
     * Credit to SkyblockAddons
     *
     * @param unformatted The actionbar text
     */
    static void updateStack(String unformatted) {
        String[] splitMessage = unformatted.split(" {3,}");
        for (String section : splitMessage) {
            String stripColor = TextUtils.stripColor(section);
            if (stripColor.contains("❤")) {
                if (stripColor.endsWith(CRIMSON.getSymbol())) {
                    String[] secondSplit = stripColor.split(" ");
                    String numAndSymbol = secondSplit[secondSplit.length - 1];
                    int newStack = Integer.parseInt(numAndSymbol.substring(0, numAndSymbol.length() - 1));
                    if (newStack != stack) {
                        restartTimer();
                    }
                    stack = newStack;
                } else {
                    stack = 0;
                    stopTimer = true;
                }
            }
        }
    }

    static void restartTimer() {
        int numPiecesCrimson = ArmorUtils.countPiecesFromSet("CRIMSON");
        if (numPiecesCrimson < 2) {
            stopTimer = true;
            return;
        }
        stopTimer = false;
        int seconds = 1 + (numPiecesCrimson - 1) * 3;
        lastStackUpdate = System.currentTimeMillis();
        stackLoseTime = lastStackUpdate + seconds * 1000L;
    }

    /**
     * Checks if kills have been gained on an item by checking book of stats
     *
     * Credit to DulkirMod
     *
     * @return true if the same item has book of stats kills increased
     */
    static boolean updateKills() {
        int kill = -1;
        String id = "";

        if (mc.thePlayer == null) {
            return false;
        }

        ItemStack stack = mc.thePlayer.getHeldItem();

        if (stack == null) {
            return false;
        }

        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("ExtraAttributes", 10) && tag.hasKey("display", 10)) {
                NBTTagCompound extra = tag.getCompoundTag("ExtraAttributes");
                NBTTagCompound display = tag.getCompoundTag("display");
                int rarityUpgrades = 0;
                if (extra.hasKey("id", 8)) {
                    id = extra.getString("id");
                }
                if (extra.hasKey("stats_book", 99)) {
                    kill = extra.getInteger("stats_book");
                }
                if (extra.hasKey("rarity_upgrades", 99)) {
                    rarityUpgrades = extra.getInteger("rarity_upgrades");
                }
                if (display.hasKey("Lore", 9)) {
                    NBTTagList lore = display.getTagList("Lore", 8);
                    String[] itemTypeArr = TextUtils.stripColor(lore.getStringTagAt(lore.tagCount() - 1)).trim().split(" ");
                    if (itemTypeArr[itemTypeArr.length - (rarityUpgrades == 0 ? 1 : 2)].equals("BOW")) {
                        return false;
                    }
                }
            }
        }

        if (!id.equals(oldID)) {
            // Check if this new item is valid
            // That is, to be specific, check that it has champion and book of stats.
            // If it doesn't, don't reset because it can't be used anyway.
            if (kill == -1) {
                return false;
            }
            // If we get here this is a new item that is legitimate for counting kills, in theory.
            oldID = id;
            oldKill = kill;
            return false;
        }

        // If this section of the code is reached, then we have the same item, and we can check for updated stats
        int killDiff = kill - oldKill;
        oldKill = kill;
        return killDiff > 0;
    }

    /**
     * Updates the timer if kills were gained or stack time ran out
     */
    public static void dominusTimer() {
        if (!LexiConfig.dominusHud.isEnabled()) return;
        if (updateKills()) {
            restartTimer();
        } else if (System.currentTimeMillis() > stackLoseTime && !stopTimer) {
            stopTimer = true;
        }
    }

}
