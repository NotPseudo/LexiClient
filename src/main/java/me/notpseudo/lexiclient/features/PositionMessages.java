package me.notpseudo.lexiclient.features;

import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.utils.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.notpseudo.lexiclient.LexiClient.mc;
import static me.notpseudo.lexiclient.utils.TextUtils.*;

public class PositionMessages {

    private static long ssLastSend, ee2LastSend, ee3LastSend, ee4LastSend, coreLastSend;
    private static long lastSS, lastEE2, lastEE3, lastEE4, lastCore, lastMelody = 0;

    private static long endEETime = 0;

    private static long endMelodyTime = 0;

    private static String eeTitle;

    private static String melodyTitle;

    public static long getEndEETime() {
        return endEETime;
    }

    public static long getEndMelodyTime() {
        return endMelodyTime;
    }

    public static String getEETitle() {
        return eeTitle;
    }

    public static String getMelodyTitle() {
        return melodyTitle;
    }

    public static void checkPosition() {
        if (!LexiConfig.sendPosMessages) return;
        if (!SBInfo.getMode().equals("dungeon") && !LexiConfig.testMode) return;
        if (mc.thePlayer == null) return;
        Vec3 posVec = mc.thePlayer.getPositionVector();
        long curTime = System.currentTimeMillis();
        if (curTime - ssLastSend > (LexiConfig.ssSendTimeout * 1000L)) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ssx, LexiConfig.ssy, LexiConfig.ssz, LexiConfig.ssx2, LexiConfig.ssy2, LexiConfig.ssz2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ssMessage);
                ssLastSend = curTime;
                return;
            }
        }
        if (curTime - ee2LastSend > (LexiConfig.ee2SendTimeout * 1000L)) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ee2x, LexiConfig.ee2y, LexiConfig.ee2z, LexiConfig.ee2x2, LexiConfig.ee2y2, LexiConfig.ee2z2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ee2Message);
                ee2LastSend = curTime;
                return;
            }
        }
        if (curTime - ee3LastSend > (LexiConfig.ee3SendTimeout * 1000L)) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ee3x, LexiConfig.ee3y, LexiConfig.ee3z, LexiConfig.ee3x2, LexiConfig.ee3y2, LexiConfig.ee3z2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ee3Message);
                ee3LastSend = curTime;
                return;
            }
        }
        if (curTime - ee4LastSend > (LexiConfig.ee4SendTimeout * 1000L)) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ee4x, LexiConfig.ee4y, LexiConfig.ee4z, LexiConfig.ee4x2, LexiConfig.ee4y2, LexiConfig.ee4z2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ee4Message);
                ee4LastSend = curTime;
                return;
            }
        }
        if (curTime - coreLastSend > (LexiConfig.coreSendTimeout * 1000L)) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.corex, LexiConfig.corey, LexiConfig.corez, LexiConfig.corex2, LexiConfig.corey2, LexiConfig.corez2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.coreMessage);
                coreLastSend = curTime;
                return;
            }
        }
    }

    private static final String PARTY_CHAT_REGEX = "^Party > (?:\\[.*?\\] )?([^:]+): (.+)$";

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type != 0) return;
        boolean posEnabled = LexiConfig.posMessageHud.isEnabled(), melodyEnabled = LexiConfig.melodyHud.isEnabled();
        if (!posEnabled && !melodyEnabled) return;
        if (!SBInfo.getMode().equals("dungeon") && !LexiConfig.testMode) return;

        String unformatted = event.message.getUnformattedText();
        String stripped = TextUtils.stripColor(unformatted);
        Pattern pattern = Pattern.compile(PARTY_CHAT_REGEX);
        Matcher matcher = pattern.matcher(stripped);

        if (matcher.matches()) {
            String username = matcher.group(1);
            if (username.equals(mc.thePlayer.getName())) return;
            String message = matcher.group(2);
            if (posEnabled) {
                if (message.equalsIgnoreCase(LexiConfig.ssTitleMessage)) {
                    handleEETitle(1, username);
                } else if (message.equalsIgnoreCase(LexiConfig.ee2TitleMessage)) {
                    handleEETitle(2, username);
                } else if (message.equalsIgnoreCase(LexiConfig.ee3TitleMessage)) {
                    handleEETitle(3, username);
                } else if (message.equalsIgnoreCase(LexiConfig.ee4TitleMessage)) {
                    handleEETitle(4, username);
                } else if (message.equalsIgnoreCase(LexiConfig.coreTitleMessage)) {
                    handleEETitle(5, username);
                }
            }
            if (melodyEnabled) {
                if (message.toLowerCase().contains("melody")) {
                    handleMelodyTitle(username);
                }
            }
        }
    }

    private static void handleEETitle(int part, String username) {
        String place = "somehow breaking this feature";
        long curTime = System.currentTimeMillis();
        switch (part) {
            case 1:
                if (curTime - lastSS < (LexiConfig.ssRecTimeout * 1000L)) {
                    return;
                }
                place = LexiConfig.ssMessage;
                lastSS = curTime;
                break;
            case 2:
                if (curTime - lastEE2 < (LexiConfig.ee2RecTimeout * 1000L)) {
                    return;
                }
                place = LexiConfig.ee2Message;
                lastEE2 = curTime;
                break;
            case 3:
                if (curTime - lastEE3 < (LexiConfig.ee3RecTimeout * 1000L)) {
                    return;
                }
                place = LexiConfig.ee3Message;
                lastEE3 = curTime;
                break;
            case 4:
                if (curTime - lastEE4 < (LexiConfig.ee4RecTimeout * 1000L)) {
                    return;
                }
                place = LexiConfig.ee4Message;
                lastEE4 = curTime;
                break;
            case 5:
                if (curTime - lastCore < (LexiConfig.coreRecTimeout * 1000L)) {
                    return;
                }
                place = LexiConfig.coreMessage;
                lastCore = curTime;
                break;
        }
        eeTitle = getColorCode(LexiConfig.positionTitleColor) + username + " is " + place;
        endEETime = System.currentTimeMillis() + (LexiConfig.posTitleDisplayTime * 1000L);
        if (LexiConfig.recPosSound) SoundUtils.playPling(LexiConfig.recPosSoundVolume, 1f); // F sharp
    }

    private static void handleMelodyTitle(String username) {
        long curTime = System.currentTimeMillis();
        if (curTime - lastMelody < (LexiConfig.melodyRecTimeout * 1000L)) return;
        lastMelody = curTime;
        melodyTitle = getColorCode(LexiConfig.melodyTitleColor) + username + " has Melody";
        endMelodyTime = System.currentTimeMillis() + (LexiConfig.melodyTitleDisplayTime * 1000L);
        if (LexiConfig.recMelodySound) {
            SoundUtils.playPling(LexiConfig.recMelodySoundVolume, .749154f); // C sharp
            SoundUtils.playPling(LexiConfig.recMelodySoundVolume, 1.122462f); // G sharp
            SoundUtils.playPling(LexiConfig.recMelodySoundVolume, 1.498307f); // C sharp
        }
    }

    private static boolean isVecInAABB(Vec3 vec, AxisAlignedBB aabb) {
        return vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX &&
                vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY &&
                vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public static void reset() {
        ssLastSend = 0;
        ee2LastSend = 0;
        ee3LastSend = 0;
        ee4LastSend = 0;
        coreLastSend = 0;
        lastSS = 0;
        lastEE2 = 0;
        lastEE3 = 0;
        lastEE4 = 0;
        lastCore = 0;
        endEETime = 0;
        endMelodyTime = 0;
    }

}
