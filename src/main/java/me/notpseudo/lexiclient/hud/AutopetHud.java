package me.notpseudo.lexiclient.hud;

import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.AutopetAlert;

public class AutopetHud extends NotificationHud {

    public AutopetHud() {
        super();
    }

    @Override
    protected void getLines(java.util.List<String> lines, boolean example) {
        if (example) {
            String equipped = LexiConfig.autopetIncludeEquipped ? "Equipped " : "";
            String level = LexiConfig.autopetShowLevel ? "§7[Lvl 100] " : "";
            String pet = "§6Bob";
            lines.add(equipped + level + pet);
            return;
        }
        lines.add(AutopetAlert.getNotification());
    }

}
