package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.TextHud;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.RelicSpawnTimer;

import java.util.List;

public class RelicSpawnHud extends NotificationHud {

    public RelicSpawnHud() {
        super();
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example || RelicSpawnTimer.stillCounting()) {
            int showTicks = Math.max(RelicSpawnTimer.getTicksLeft(), 0);
            if (LexiConfig.relicSpawnInSecs) {
                lines.add(String.format("%.2fs", (showTicks * .05)));
            } else {
                lines.add(showTicks + "t");
            }
        }
    }

}
