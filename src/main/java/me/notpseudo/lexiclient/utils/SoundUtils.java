package me.notpseudo.lexiclient.utils;

import net.minecraft.client.audio.SoundCategory;

import static me.notpseudo.lexiclient.LexiClient.mc;

public class SoundUtils {

    public static void playPling(float volume, float pitch) {
        float prevMaster = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 1f);
        float prevNote = mc.gameSettings.getSoundLevel(SoundCategory.RECORDS);
        mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, 1f);
        mc.thePlayer.playSound(
                "note.pling",
                volume,
                pitch
        );
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, prevMaster);
        mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, prevNote);
    }

}
