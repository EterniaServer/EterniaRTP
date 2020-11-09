package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eterniartp.enums.ConfigStrings;
import br.com.eterniaserver.eterniartp.enums.Messages;

import org.bukkit.command.CommandSender;

import java.io.File;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaRTP";
    public static final String DATA_LOCALE_FOLDER_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "locales";
    public static final String CONFIG_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "config.yml";
    public static final String MESSAGES_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "messages.yml";
    public static final String COMMANDS_FILE_PATH = DATA_LOCALE_FOLDER_PATH + File.separator + "commands.yml";

    protected static void sendMessage(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        sender.sendMessage(EterniaRTP.getMessage(messagesId, prefix, args));
    }

    protected static String getMessage(Messages messagesId, boolean prefix, String[] messages, String... args) {
        String message = messages[messagesId.ordinal()];

        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }

        if (prefix) {
            return EterniaRTP.getString(ConfigStrings.SERVER_PREFIX) + message;
        }

        return message;
    }

}
