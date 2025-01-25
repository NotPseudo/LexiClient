package me.notpseudo.lexiclient;

import me.notpseudo.lexiclient.command.LexiClientCommand;
import me.notpseudo.lexiclient.features.DominusTimer;
import me.notpseudo.lexiclient.features.DumbMessages;
import me.notpseudo.lexiclient.features.PositionMessages;
import me.notpseudo.lexiclient.utils.SBInfo;
import me.notpseudo.lexiclient.utils.TabListUtils;
import me.notpseudo.lexiclient.utils.TitleUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import me.notpseudo.lexiclient.config.LexiConfig;
import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import net.minecraftforge.fml.common.Mod;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = LexiClient.MODID, name = LexiClient.NAME, version = LexiClient.VERSION, clientSideOnly = true)
public class LexiClient {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.

    public static final String CHAT_PREFIX = "§f<§3LexiClient§f>§r";

    public static Minecraft mc;

    @Mod.Instance(MODID)
    public static LexiClient INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static LexiConfig config;

    private long lastLongUpdate = 0L;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new LexiConfig();
        mc = Minecraft.getMinecraft();
        CommandManager.INSTANCE.registerCommand(new LexiClientCommand());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DominusTimer());
        MinecraftForge.EVENT_BUS.register(new SBInfo());
        MinecraftForge.EVENT_BUS.register(new TitleUtils());
        MinecraftForge.EVENT_BUS.register(new PositionMessages());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        long currentTime = System.currentTimeMillis();
        DominusTimer.dominusTimer();
        PositionMessages.checkPosition();
        if (currentTime - lastLongUpdate > 1000) {
            TabListUtils.parseTabEntries();
            DumbMessages.dumbMessage();
            lastLongUpdate = currentTime;
        }
    }

}
