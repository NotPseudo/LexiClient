package me.notpseudo.lexiclient.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommandGroup;
import me.notpseudo.lexiclient.LexiClient;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import me.notpseudo.lexiclient.features.GiftNotifications;
import me.notpseudo.lexiclient.features.PositionMessages;
import me.notpseudo.lexiclient.utils.ChatUtils;

import java.util.Set;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in LexiClient.java with `CommandManager.INSTANCE.registerCommand(new LexiClientCommand());`
 *
 * @see Command
 * @see Main
 * @see LexiClient
 */
@Command(value = LexiClient.MODID, aliases = {"lexi", "lc"}, description = "Access " + LexiClient.NAME)
public class LexiClientCommand {

    @SubCommand(description = "Prints the help and command descriptions", aliases = {"h"})
    private void help() {
        ChatUtils.info("/lexi - Opens the GUI");
        ChatUtils.info("/lexi help - Displays this help text");
        ChatUtils.info("/lexi load gift - Loads the gift config file");
        ChatUtils.info("/lexi load posmessage - Loads the posmessage config file");
    }

    @SubCommandGroup(value = "load", aliases = "l")
    private class LoadGroup {

        @SubCommand(description = "Loads the gifts you care about config file from config/lexiclient/goodgifts.txt", aliases = {"gifts", "goodgifts"})
        private void gift() {
            Set<String> loaded = GiftNotifications.loadGoodGifts();
            if (loaded == null) {
                ChatUtils.error("Failed to load gifts");
            } else {
                ChatUtils.success("Loaded gifts: " + loaded);
            }
        }

        @SubCommand(description = "Loads the positional messages config file from config/lexiclient/positional_messages.json", aliases = {"pm", "posmessages", "positionalmessages"})
        private void posmessage() {
            if (PositionMessages.loadPosMessageConfig()) {
                ChatUtils.success("Loaded positional messages");
            } else {
                ChatUtils.error("Failed to load positional messages");
            }

        }

    }

    @Main(description = "Opens the GUI")
    private void handle() {
        LexiClient.INSTANCE.config.openGui();
    }
}