package me.notpseudo.lexiclient.utils;

import net.minecraft.client.audio.SoundCategory;

import java.util.Arrays;
import java.util.List;

import static me.notpseudo.lexiclient.LexiClient.mc;

public class SoundUtils {


    private static final List<SoundCategory> CATEGORIES = Arrays.asList(SoundCategory.values());

    public static void playPling(float volume, float pitch) {
        playSound("note.pling", volume, pitch);
    }

    public static void playSound(String sound, float volume, float pitch) {
        if (mc.thePlayer == null) return;
        float[] prevVolumes = new float[CATEGORIES.size()];
        CATEGORIES.forEach(category -> {
            prevVolumes[category.ordinal()] = mc.gameSettings.getSoundLevel(category);
        });
        CATEGORIES.forEach(category -> {
            mc.gameSettings.setSoundLevel(category, 1f);
        });
        mc.thePlayer.playSound(sound, volume, pitch);
        CATEGORIES.forEach(category -> {
            mc.gameSettings.setSoundLevel(category, prevVolumes[category.ordinal()]);
        });
    }

}
