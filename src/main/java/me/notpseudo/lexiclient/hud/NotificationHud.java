package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.TextHud;

import java.util.List;

public abstract class NotificationHud extends TextHud {

    public NotificationHud() {
        super(false);
    }

    @Override
    public boolean shouldDrawBackground() {
        return false;
    }

    @Override
    protected abstract void getLines(List<String> lines, boolean example);

}
