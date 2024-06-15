package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.TextHud;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.DominusTimer;

import java.util.List;

/**
 * An example OneConfig HUD that is started in the config and displays text.
 *
 * @see LexiConfig#dominusHud
 */
public class DominusHud extends TextHud {

    public DominusHud() {
        super(false);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        lines.add(String.format("Stack Time Left: %.2f", DominusTimer.stopTimer ? 0 : (DominusTimer.stackLoseTime - System.currentTimeMillis()) / 1000.0));
        lines.add("Stack: "+ DominusTimer.stack);
    }
}
