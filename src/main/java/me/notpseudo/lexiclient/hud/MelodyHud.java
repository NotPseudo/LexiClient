package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.TextHud;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.PositionMessages;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.List;

import static me.notpseudo.lexiclient.LexiClient.mc;
import static me.notpseudo.lexiclient.utils.TextUtils.*;

public class MelodyHud extends TextHud {

    public MelodyHud() {
        super(false);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example) {
            EntityPlayerSP player = mc.thePlayer;
            String username = player == null ? "Steve" : player.getName();
            lines.add(getColorCode(LexiConfig.melodyTitleColor) + username + " has Melody");
            return;
        }
        if (PositionMessages.getEndMelodyTime() < System.currentTimeMillis()) return;
        lines.add(PositionMessages.getMelodyTitle());
    }
}
