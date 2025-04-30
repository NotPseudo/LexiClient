package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.GiftNotifications;

import java.util.List;

public class GiftHud extends NotificationHud {

    public GiftHud() {
        super();
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example) {
            lines.add("§6Valueable Item");
            if (LexiConfig.showSinceClear) lines.add("Good Items Since Last Clear: 4");
            if (LexiConfig.showSeparateGiftDrops) {
                lines.add("§6Valueable Item 1: §r1");
                lines.add("§cValueable Item 2: §r2");
                lines.add("§5Valueable Item 3: §r1");
            }
            return;
        }
        lines.addAll(GiftNotifications.getHudStrings());
    }

}
