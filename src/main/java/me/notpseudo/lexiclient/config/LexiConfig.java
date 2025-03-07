package me.notpseudo.lexiclient.config;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Number;
import me.notpseudo.lexiclient.LexiClient;
import cc.polyfrost.oneconfig.config.Config;

import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import me.notpseudo.lexiclient.features.PositionMessages;
import me.notpseudo.lexiclient.features.RelicSpawnTimer;
import me.notpseudo.lexiclient.hud.DominusHud;
import me.notpseudo.lexiclient.hud.MelodyHud;
import me.notpseudo.lexiclient.hud.PosMessageHud;
import me.notpseudo.lexiclient.hud.RelicSpawnHud;
import me.notpseudo.lexiclient.utils.ChatUtils;
import net.minecraft.client.audio.SoundCategory;

import static me.notpseudo.lexiclient.LexiClient.mc;


/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class LexiConfig extends Config {


    @HUD(
            name = "Position Titles",
            category = "Dungeons",
            subcategory = "Position Titles"
    )
    public static PosMessageHud posMessageHud = new PosMessageHud();

    @Number(
            name = "Position Title Display Time",
            min = 2, max = 10,
            description = "The number of seconds to display the title for after receiving a positional message",
            step = 1,
            category = "Dungeons", subcategory = "Position Titles"
    )
    public static int posTitleDisplayTime = 5;

    @Switch(
            name = "Play Sound for Positional",
            description = "Will play a sound when receiving positional messages",
            category = "Dungeons",
            subcategory = "Position Titles"
    )
    public static boolean recPosSound = false;

    @Slider(
            name = "Positional Sound Volume",
            description = "Volume of receive positional message sound",
            category = "Dungeons",
            subcategory = "Position Titles",
            min = 0f,
            max = 1f
    )
    public static float recPosSoundVolume = .7f;

    @Button(
            name = "Positional Volume Test",
            category = "Dungeons",
            subcategory = "Position Titles",
            text = "Test Sound",
            size = 2
    )
    void demoPosTitleVolume() {
        float prevMaster = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 1f);
        float prevNote = mc.gameSettings.getSoundLevel(SoundCategory.RECORDS);
        mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, 1f);
        mc.thePlayer.playSound(
                "note.pling",
                recPosSoundVolume,
                1f
        );
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, prevMaster);
        mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, prevNote);
    }

    @Dropdown(
            name = "Position Title Color",
            options = {"Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red", "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue", "Green", "Aqua", "Red", "Light Purple", "Yellow", "White"},
            description = "The color of the title that displays",
            category = "Dungeons",
            subcategory = "Position Titles",
            size = 2
    )
    public static int positionTitleColor = 6;

    @Switch(
            name = "Send Positional Messages",
            description = "Automatically send messages when your player goes within coordinates",
            category = "Dungeons",
            subcategory = "Send Positional Messages"
    )
    public static boolean sendPosMessages = false;

    @Button(
            name = "Reload Positional Messages",    // name beside the button
            text = "Reload",
            description = "Reload Positional Messages after you change the config file", // text on the button itself
            category = "Dungeons",
            subcategory = "Send Positional Messages"
    )
    static void reloadPosMessages() {    // using a lambda to create the runnable interface.
        PositionMessages.loadPosMessageConfig();
    };

    @Button(
            name = "Restore Default Positional Messages",    // name beside the button
            text = "Reset",
            description = "Reset default coordinates and messages for Send Positional Messages", // text on the button itself
            category = "Dungeons",
            subcategory = "Send Positional Messages"
    )
    static void resetSendPosMessages() {    // using a lambda to create the runnable interface.
        PositionMessages.saveDefaultPosMessages();
    };

    @HUD(
            name = "Melody Title",
            category = "Dungeons",
            subcategory = "Melody Title"
    )
    public static MelodyHud melodyHud = new MelodyHud();

    @Number(
            name = "Melody Title Display Time",
            min = 2, max = 10,
            description = "The number of seconds to display the title for after receiving a melody message",
            step = 1,
            category = "Dungeons", subcategory = "Melody Title"
    )
    public static int melodyTitleDisplayTime = 5;

    @Number(
            name = "Melody Title Timeout",
            min = 2, max = 20,
            description = "The number of seconds to wait after the last received melody message before rendering the title again",
            step = 1,
            category = "Dungeons", subcategory = "Melody Title"
    )
    public static int melodyRecTimeout = 5;

    @Dropdown(
            name = "Melody Title Color",
            options = {"Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red", "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue", "Green", "Aqua", "Red", "Light Purple", "Yellow", "White"},
            description = "The color of the title that displays",
            category = "Dungeons",
            subcategory = "Melody Title",
            size = 2
    )
    public static int melodyTitleColor = 11;

    @Switch(
            name = "Play Sound for Melody",
            description = "Will play a sound when receiving melody messages",
            category = "Dungeons",
            subcategory = "Melody Title"
    )
    public static boolean recMelodySound = false;

    @Slider(
            name = "Melody Sound Volume",
            description = "Volume of receive melody message sound",
            category = "Dungeons",
            subcategory = "Melody Title",
            min = 0f,
            max = 1f,
            step = 0
    )
    public static float recMelodySoundVolume = .7f;

    @Button(
            name = "Melody Volume Test",
            category = "Dungeons",
            subcategory = "Melody Title",
            text = "Test Sound",
            size = 2
    )
    void demoMelodyTitleVolume() {
        float prevMaster = mc.gameSettings.getSoundLevel(SoundCategory.MASTER);
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 1f);
        float prevNote = mc.gameSettings.getSoundLevel(SoundCategory.RECORDS);
        mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, 1f);
        mc.thePlayer.playSound("note.pling", recPosSoundVolume, .749154f);
        mc.thePlayer.playSound("note.pling", recPosSoundVolume, 1.122462f);
        mc.thePlayer.playSound("note.pling", recPosSoundVolume, 1.498307f);
        mc.gameSettings.setSoundLevel(SoundCategory.MASTER, prevMaster);
        mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, prevNote);
    }

    @HUD(
            name = "Relic Spawn Timer",
            category = "Dungeons",
            subcategory = "Relic Spawn Timer"
    )
    public static RelicSpawnHud relicSpawnHud = new RelicSpawnHud();

    @Number(
            name = "Relic Spawn Ticks",
            description = "The number of ticks after Necron defeat message until relics spawn",
            category = "Dungeons",
            subcategory = "Relic Spawn Timer",
            min = 20, max = 200, step = 20
    )
    public static int relicSpawnTicks = 42;

    @Switch(
            name = "Display Time in Seconds",
            description = "If enabled, will display the spawn time in seconds. If disabled, will display time in ticks",
            category = "Dungeons",
            subcategory = "Relic Spawn Timer"
    )
    public static boolean relicSpawnInSecs = false;

    @Button(
            name = "Test Relic Timer", text = "Test", category = "Dungeons", subcategory = "Relic Spawn Timer"
    )
    Runnable testRelicTimer = RelicSpawnTimer::testRelicSpawnTimer;

    @HUD(
            name = "Dominus Hud",
            category = "Crimson Stack"
    )
    public static DominusHud dominusHud = new DominusHud();

    @Switch(
            name = "Lexi Rat",
            category = "Dumb Shit"
    )
    public static boolean lexiRat = false;

    @Switch(
            name = "Dumb Messages",
            category = "Dumb Shit"
    )
    public static boolean dumbMessages = false;

    @Slider(
            name = "Dumb Message Timer",
            category = "Dumb Shit",
            min = 15f, max = 120f,
            step = 15
    )
    public static float dumbMessageTime = 15f;

    @Switch(
            name = "Test Mode",
            description = "Do not enable as this will break many features",
            category = "Testing"
    )
    public static boolean testMode = false;

    @Text(
            name = "Test Message",
            description = "The message to send to your client",
            category = "Testing"
    )
    public static String testMessage = "";

    @Button(
            name = "Test Message", text = "Test", category = "Testing"
    )
    Runnable sendTestMessage = () -> {
        ChatUtils.info(testMessage, false);
    };

    public LexiConfig() {
        super(new Mod(LexiClient.NAME, ModType.SKYBLOCK), LexiClient.MODID + ".json");
        initialize();
        addDependency("reloadPosMessages", "sendPosMessages");
        addDependency("resetSendPosMessages", "sendPosMessages");
        addDependency("testMessage", "testMode");
        addDependency("sendTestMessage", "testMode");
    }
}

