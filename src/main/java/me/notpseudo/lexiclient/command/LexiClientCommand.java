package me.notpseudo.lexiclient.command;

import me.notpseudo.lexiclient.LexiClient;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in LexiClient.java with `CommandManager.INSTANCE.registerCommand(new LexiClientCommand());`
 *
 * @see Command
 * @see Main
 * @see LexiClient
 */
@Command(value = LexiClient.MODID, aliases = {"lexi", "lc"}, description = "Access the " + LexiClient.NAME + " GUI.")
public class LexiClientCommand {
    @Main
    private void handle() {
        LexiClient.INSTANCE.config.openGui();
    }
}