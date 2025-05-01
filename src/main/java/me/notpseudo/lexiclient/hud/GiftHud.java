package me.notpseudo.lexiclient.hud;

import me.notpseudo.lexiclient.LexiClient;
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
            if (LexiConfig.showItemsSinceClear) lines.add("Good Items Since Last Clear: 3");
            if (LexiConfig.showSinceClear) lines.add("Good Drops Since Last Clear: 7");
            if (LexiConfig.showSeparateGiftDrops) {
                lines.add("§6Valueable Item 1: 1");
                lines.add("§cValueable Drop 2: 4");
                lines.add("§5Valueable Item 3: 2");
            }
            return;
        }
        lines.addAll(GiftNotifications.getHudStrings());
    }

}
