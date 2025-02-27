package me.notpseudo.lexiclient.hud;

import cc.polyfrost.oneconfig.hud.TextHud;
import me.notpseudo.lexiclient.config.LexiConfig;
import me.notpseudo.lexiclient.features.PositionMessages;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.List;

import static me.notpseudo.lexiclient.LexiClient.mc;
import static me.notpseudo.lexiclient.utils.TextUtils.*;

public class PosMessageHud extends TextHud {

    public PosMessageHud() {
        super(false);
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (example) {
            EntityPlayerSP player = mc.thePlayer;
            String username = player == null ? "Steve" : player.getName();
            lines.add(getColorCode(LexiConfig.positionTitleColor) + username + " is " + LexiConfig.ee2TitleMessage);
            return;
        }
        if (PositionMessages.getEndEETime() < System.currentTimeMillis()) return;
        lines.add(PositionMessages.getEETitle());
    }
}
