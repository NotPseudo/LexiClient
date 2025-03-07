package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.TextHud;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.RelicSpawnTimer;

import java.util.List;

public class RelicSpawnHud extends TextHud {

    public RelicSpawnHud() {
        super(false);
    }

    @Override
    public boolean shouldDrawBackground() {
        return false;
    }

        @Override
    protected void getLines(List<String> lines, boolean example) {
        int showTicks = Math.max(RelicSpawnTimer.getTicksLeft(), 0);
        if (example || RelicSpawnTimer.stillCounting()) {
            if (LexiConfig.relicSpawnInSecs) {
                lines.add(String.format("%.2fs", (showTicks * .05)));
            } else {
                lines.add(showTicks + "t");
            }
        }
    }

}
