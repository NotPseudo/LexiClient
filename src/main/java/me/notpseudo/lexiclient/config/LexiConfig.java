package me.notpseudo.lexiclient.config;

import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import me.notpseudo.lexiclient.LexiClient;
import cc.polyfrost.oneconfig.config.Config;

import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import me.notpseudo.lexiclient.hud.DominusHud;


/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class LexiConfig extends Config {

    @HUD(
            name = "Dominus Hud",
            category = "Hud"
    )
    public static DominusHud dominusHud = new DominusHud();

    @Switch(
            name = "Lexi Rat",
            category = "Dumb Shit"
    )
    public static boolean lexiRat = true;

    @Switch(
            name = "Dumb Messages",
            category = "Dumb Shit"
    )
    public static boolean dumbMessages = true;

    @Slider(
            name = "Dumb Message Timer",
            category = "Dumb Shit",
            min = 15f, max = 120f,
            step = 15
    )
    public static float dumbMessageTime = 15f;


    public LexiConfig() {
        super(new Mod(LexiClient.NAME, ModType.SKYBLOCK), LexiClient.MODID + ".json");
        initialize();
    }
}

