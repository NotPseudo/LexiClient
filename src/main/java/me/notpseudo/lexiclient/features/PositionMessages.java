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

public class PositionMessages {

    private static long ssLastSend, ee2LastSend, ee3LastSend, ee4LastSend, coreLastSend = 0;
    private static long lastSS, lastEE2, lastEE3, lastEE4, lastCore = 0;

    public static void checkPosition() {
        if (!LexiConfig.sendPosMessages) return;
        if (!SBInfo.getMode().equals("dungeon") && !LexiConfig.testMode) return;
        if (mc.thePlayer == null) return;
        Vec3 posVec = mc.thePlayer.getPositionVector();
        long curTime = System.currentTimeMillis();
        if (curTime - ssLastSend > 20000) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ssx, LexiConfig.ssy, LexiConfig.ssz, LexiConfig.ssx2, LexiConfig.ssy2, LexiConfig.ssz2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ssMessage);
                ssLastSend = curTime;
                return;
            }
        }
        if (curTime - ee2LastSend > 20000) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ee2x, LexiConfig.ee2y, LexiConfig.ee2z, LexiConfig.ee2x2, LexiConfig.ee2y2, LexiConfig.ee2z2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ee2Message);
                ee2LastSend = curTime;
                return;
            }
        }
        if (curTime - ee3LastSend > 20000) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ee3x, LexiConfig.ee3y, LexiConfig.ee3z, LexiConfig.ee3x2, LexiConfig.ee3y2, LexiConfig.ee3z2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ee3Message);
                ee3LastSend = curTime;
                return;
            }
        }
        if (curTime - ee4LastSend > 20000) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.ee4x, LexiConfig.ee4y, LexiConfig.ee4z, LexiConfig.ee4x2, LexiConfig.ee4y2, LexiConfig.ee4z2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.ee4Message);
                ee4LastSend = curTime;
                return;
            }
        }
        if (curTime - coreLastSend > 20000) {
            if (isVecInAABB(posVec, new AxisAlignedBB(LexiConfig.corex, LexiConfig.corey, LexiConfig.corez, LexiConfig.corex2, LexiConfig.corey2, LexiConfig.corez2))) {
                ChatUtils.sendPartyChatMessage(LexiConfig.coreMessage);
                coreLastSend = curTime;
                return;
            }
        }
    }

    private static final String CHAT_REGEX = "^Party > (?:\\[.*?\\] )?([^:]+): (.+)$";

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (event.type != 0) return;
        if (!LexiConfig.showPosTitle) return;
        if (!SBInfo.getMode().equals("dungeon") && !LexiConfig.testMode) return;

        String unformatted = event.message.getUnformattedText();
        String stripped = TextUtils.stripColor(unformatted);
        Pattern pattern = Pattern.compile(CHAT_REGEX);
        Matcher matcher = pattern.matcher(stripped);

        if (matcher.matches()) {
            String username = matcher.group(1);
            if (username.equals(mc.thePlayer.getName())) return;
            String message = matcher.group(2);
            if (message.equalsIgnoreCase(LexiConfig.ssTitleMessage)) {
                handleTitle(1, username);
            }
            if (message.equalsIgnoreCase(LexiConfig.ee2TitleMessage)) {
                handleTitle(2, username);
            }
            if (message.equalsIgnoreCase(LexiConfig.ee3TitleMessage)) {
                handleTitle(3, username);
            }
            if (message.equalsIgnoreCase(LexiConfig.ee4TitleMessage)) {
                handleTitle(4, username);
            }
            if (message.equalsIgnoreCase(LexiConfig.coreTitleMessage)) {
                handleTitle(5, username);
            }

        }
    }

    private static void handleTitle(int part, String username) {
        String place = "somehow breaking this feature";
        long curTime = System.currentTimeMillis();
        switch (part) {
            case 1:
                if (curTime - lastSS < 20000) {
                    return;
                }
                place = LexiConfig.ssMessage;
                lastSS = curTime;
                break;
            case 2:
                if (curTime - lastEE2 < 5000) {
                    return;
                }
                place = LexiConfig.ee2Message;
                lastEE2 = curTime;
                break;
            case 3:
                if (curTime - lastEE3 < 5000) {
                    return;
                }
                place = LexiConfig.ee3Message;
                lastEE3 = curTime;
                break;
            case 4:
                if (curTime - lastEE4 < 5000) {
                    return;
                }
                place = LexiConfig.ee4Message;
                lastEE4 = curTime;
                break;
            case 5:
                if (curTime - lastCore < 20000) {
                    return;
                }
                place = LexiConfig.coreMessage;
                lastCore = curTime;
                break;
        }
        String title = getColorCode(LexiConfig.positionTitleColor) + username + " is " + place;
        if (LexiConfig.posTitleUseCustomScreen) {
            TitleUtils.showTitleForTime(title, 5000, LexiConfig.posTitleX, LexiConfig.posTitleY, LexiConfig.positionTitleSize);
        } else {
            TitleUtils.showTitleForTime(title, 5000, LexiConfig.positionTitleSize);
        }
        if (LexiConfig.recPosSound) SoundUtils.playPling(LexiConfig.recPosSoundVolume);
    }

    private static String getColorCode(int colorOption) {
        if (colorOption < 10 && colorOption > -1) return "§" + colorOption;
        char color = (char) ('a' + (colorOption - 10));
        return "§" + color;
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
    }

    private static boolean isVecInAABB(Vec3 vec, AxisAlignedBB aabb) {
        return vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX &&
                vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY &&
                vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

}
