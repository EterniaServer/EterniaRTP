package br.com.eterniaserver.eterniartp;

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

}
