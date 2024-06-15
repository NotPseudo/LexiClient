package me.notpseudo.lexiclient.utils;

import com.google.common.collect.ComparisonChain;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static me.notpseudo.lexiclient.LexiClient.mc;
import static net.minecraft.world.WorldSettings.GameType.SPECTATOR;

/**
 * Utility class to get information from the player tab
 *
 * Credit to Skytils
 */
public class TabListUtils {

    public static String area = "";

    private static Pattern areaPatern = Pattern.compile("Area: (.+)");

    private static final Ordering<NetworkPlayerInfo> playerInfoOrdering = new Ordering<NetworkPlayerInfo>() {
        @Override
        public int compare(NetworkPlayerInfo info1, NetworkPlayerInfo info2) {
            if (info1 == null) return -1;
            if (info2 == null) return 0;
            return ComparisonChain.start()
                    .compareTrueFirst(info1.getGameType() != SPECTATOR, info2.getGameType() != SPECTATOR)
                    .compare(info1.getPlayerTeam() != null ? info1.getPlayerTeam().getRegisteredName() : "",
                            info2.getPlayerTeam() != null ? info2.getPlayerTeam().getRegisteredName() : "")
                    .compare(info1.getGameProfile().getName(), info2.getGameProfile().getName())
                    .result();
        }
    };

    static List<NetworkPlayerInfo> fetchTabEntries() {
        if (mc.thePlayer == null) return emptyList();
        return playerInfoOrdering.sortedCopy(mc.thePlayer.sendQueue.getPlayerInfoMap());
    }

    public static void parseTabEntries() {
        final List<String> scoreboardList = fetchTabEntries().stream()
                .map(info -> info.getDisplayName() != null ? info.getDisplayName().getUnformattedText() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (String line : scoreboardList) {
            String trimmed = line.trim();
            Matcher matcher = areaPatern.matcher(trimmed);
            if (matcher.matches()) {
                area = matcher.group(1);
            }
        }
    }

}
