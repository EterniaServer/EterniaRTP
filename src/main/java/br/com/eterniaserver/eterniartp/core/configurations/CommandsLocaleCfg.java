package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.core.enums.Commands;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class CommandsLocaleCfg implements ReloadableConfiguration {

    private final CommandLocale[] commandLocales;

    private final FileConfiguration inFile;
    private final FileConfiguration outFile;

    public CommandsLocaleCfg() {
        this.commandLocales = new CommandLocale[Commands.values().length];

        this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        this.outFile = new YamlConfiguration();
    }

    @Override
    public FileConfiguration inFileConfiguration() {
        return inFile;
    }

    @Override
    public FileConfiguration outFileConfiguration() {
        return outFile;
    }

    @Override
    public String getFolderPath() {
        return Constants.DATA_LOCALE_FOLDER_PATH;
    }

    @Override
    public String getFilePath() {
        return Constants.COMMANDS_FILE_PATH;
    }

    @Override
    public String[] messages() {
        return new String[0];
    }

    @Override
    public CommandLocale[] commandsLocale() {
        return commandLocales;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.BLOCKED;
    }

    @Override
    public void executeConfig() { }

    @Override
    public void executeCritical() {
        addCommandLocale(
                Commands.RTP,
                new CommandLocale(
                        "rtp",
                        null,
                        " Teleporte-se para um local randomizado",
                        "eternia.rtp",
                        null
                )
        );
        addCommandLocale(
                Commands.RTP_HELP,
                new CommandLocale(
                        "help",
                        " <página>",
                        " Receba ajuda para o sistema de RTP",
                        "eternia.rtp",
                        null
                )
        );

        loadCommandsLocale();
    }

    private void loadCommandsLocale() {
        for (Commands command : Commands.values()) {
            CommandLocale commandLocale = commandsLocale()[command.ordinal()];
            EterniaLib.getCmdManager().getCommandReplacements().addReplacements(
                    command.name().toLowerCase(), commandLocale.name(),
                    command.name().toLowerCase() + "_DESCRIPTION", commandLocale.description(),
                    command.name().toLowerCase() + "_PERM", commandLocale.perm(),
                    command.name().toLowerCase() + "_SYNTAX", commandLocale.syntax(),
                    command.name().toLowerCase() + "_ALIASES", commandLocale.aliases()
            );
        }
    }
}
