package me.notpseudo.lexiclient.config;

import cc.polyfrost.oneconfig.config.annotations.HUD;
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

    public LexiConfig() {
        super(new Mod(LexiClient.NAME, ModType.SKYBLOCK), LexiClient.MODID + ".json");
        initialize();
    }
}
