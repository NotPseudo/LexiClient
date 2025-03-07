package me.notpseudo.lexiclient.features;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.notpseudo.lexiclient.LexiClient;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.utils.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.notpseudo.lexiclient.LexiClient.mc;
import static me.notpseudo.lexiclient.utils.TextUtils.*;

public class PositionMessages {

    private static long ssLastSend, ee2LastSend, ee3LastSend, ee4LastSend, coreLastSend;
    private static long lastMelody = 0;

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

    private static final File CONFIG_FILE = new File(mc.mcDataDir, "config/positional_messages.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static List<PositionalMessage> posMessages = new ArrayList<>();

    public static void loadPosMessageConfig() {
        if (!CONFIG_FILE.exists()) {
            saveDefaultPosMessages();
        }
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Type listType = new TypeToken<List<PositionalMessage>>() {}.getType();
            posMessages = GSON.fromJson(reader, listType);
        } catch (FileNotFoundException e) {
            LexiClient.LOGGER.error("Positional messages config file not found!\n" + e.getMessage());
        } catch (IOException e) {
            LexiClient.LOGGER.error("Error reading positional messages config file!\n" + e.getMessage());
        }
    }

    private static void savePosMessages() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(posMessages, writer);
        } catch (IOException e) {
            LexiClient.LOGGER.error("Error writing to positional messages JSON file " + e.getMessage());
        }
    }

    public static void saveDefaultPosMessages() {
        posMessages.add(new PositionalMessage(107, 120, 93, 110, 120, 94, "At SS!", 60));
        posMessages.add(new PositionalMessage(55, 107, 129, 60, 110, 133, "At Early Enter 2!", 60));
        posMessages.add(new PositionalMessage(5, 110, 103, 7, 115, 107, "At Early Enter 3!", 60));
        posMessages.add(new PositionalMessage(50, 113, 50, 58, 117, 54, "At Core!", 10));
        posMessages.add(new PositionalMessage(49, 111, 55, 60, 118, 60, "In Goldor Tunnel!", 20));
        posMessages.add(new PositionalMessage(67,109,122,68,109,123, "At Early Enter 2 Safe Spot!", 60));
        posMessages.add(new PositionalMessage(18,120,92,19,121,93, "At Early Enter 3 Safe Spot!", 60));
        posMessages.add(new PositionalMessage(62,127,34,64,127,36, "At I4!", 60));
        //posMessages.add(new PositionalMessage(50,70,50,58,70,54, "At Mid!", 60));
        savePosMessages();
    }

    public static void checkPosition() {
        if (!LexiConfig.sendPosMessages) return;
        if (!SBInfo.getMode().equals("dungeon") && !LexiConfig.testMode) return;
        if (mc.thePlayer == null) return;
        Vec3 posVec = mc.thePlayer.getPositionVector();
        long curTime = System.currentTimeMillis();
        for (PositionalMessage pm : posMessages) {
            if (curTime - pm.getLastSentTime() > (pm.getTimeout() * 1000L)) {
                if (isVecInAABB(posVec, pm.getArea())) {
                    ChatUtils.sendPartyChatMessage(pm.getMessage());
                    pm.setLastSentTime(curTime);
                    return;
                }
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
            String lowercase = message.toLowerCase();
            if (posEnabled) {
                if (lowercase.startsWith("at ") || lowercase.startsWith("in ")) {
                    handleEETitle(username, message);
                }
            }
            if (melodyEnabled) {
                if (lowercase.contains("melody")) {
                    handleMelodyTitle(username);
                }
            }
        }
    }

    private static void handleEETitle(String username, String message) {
        eeTitle = getColorCode(LexiConfig.positionTitleColor) + username + " is " + message;
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
        endEETime = 0;
        endMelodyTime = 0;
    }

}
