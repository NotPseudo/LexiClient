package me.notpseudo.lexiclient.features;

import me.notpseudo.lexiclient.LexiClient;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.events.PacketEvent;
import me.notpseudo.lexiclient.events.ServerTickEvent;
import me.notpseudo.lexiclient.utils.SBInfo;
import me.notpseudo.lexiclient.utils.TextUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelicSpawnTimer {

    private static final String NECRON_DEFEAT_REGEX = "^\\[BOSS\\] Necron: All this, for nothing\\.\\.\\.$";
    private static final Pattern DEFEAT_PATTERN = Pattern.compile(NECRON_DEFEAT_REGEX);
    private static int ticksLeft = LexiConfig.relicSpawnTicks;
    private static boolean continueUpdate;

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        reset();
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        if (!LexiConfig.relicSpawnHud.isEnabled()) return;
        if (!SBInfo.getMode().equals("dungeon") && !LexiConfig.testMode) return;
        if (event.type != 0) return;
        String unformatted = event.message.getUnformattedText();
        String stripped = TextUtils.stripColor(unformatted);
        Matcher matcher = DEFEAT_PATTERN.matcher(stripped);
        if (matcher.matches()) {
            continueUpdate = true;
        }
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (!continueUpdate) return;
        if (ticksLeft-- <= -25) {
            reset();
        }
    }

    public static void testRelicSpawnTimer() {
        if (!continueUpdate) continueUpdate = true;
    }

    public static int getTicksLeft() {
        return ticksLeft;
    }

    public static boolean stillCounting() {
        return continueUpdate;
    }

    private static void reset() {
        ticksLeft = LexiConfig.relicSpawnTicks;
        continueUpdate = false;
    }

}
