package me.notpseudo.lexiclient.hud;

import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.GiftNotifications;
import me.notpseudo.lexiclient.utils.ChatUtils;

import java.util.List;

public class ItemStashHud extends NotificationHud {

    public ItemStashHud() {
        super();
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example) {
            lines.add(ChatUtils.ChatColor.RED + "Item Stash Almost Full!" + (LexiConfig.itemStashCount ? LexiConfig.itemStashLimit : ""));
            return;
        }
        lines.add(GiftNotifications.getItemStashNotification());
    }
}
