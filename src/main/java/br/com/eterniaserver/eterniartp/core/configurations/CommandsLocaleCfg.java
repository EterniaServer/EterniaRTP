package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.core.baseobjects.CommandLocale;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.core.enums.Commands;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CommandsLocaleCfg {

    private final CommandLocale[] commandLocales;

    private final String[] name;
    private final String[] syntax;
    private final String[] descriptions;
    private final String[] perms;
    private final String[] aliases;

    public CommandsLocaleCfg() {
        this.name = new String[Commands.values().length];
        this.syntax = new String[Commands.values().length];
        this.descriptions = new String[Commands.values().length];
        this.perms = new String[Commands.values().length];
        this.aliases = new String[Commands.values().length];
        this.commandLocales = new CommandLocale[Commands.values().length];

        this.addDefault(Commands.RTP, "rtp", "eternia.rtp", null, " Teleporte-se para um local randomizado");
        this.addDefault(Commands.RTP_HELP, "help", "eternia.help", " <página>", " Receba ajuda para o sistema de RTP");

        // Load and save the configurations
        final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_FILE_PATH));

        for (final Commands entry : Commands.values()) {
            final CommandLocale commandLocale = commandLocales[entry.ordinal()];

            this.name[entry.ordinal()] = config.getString(entry.name() + ".name", commandLocale.name);
            config.set(entry.name() + ".name", this.name[entry.ordinal()]);

            if (commandLocale.syntax != null) {
                this.syntax[entry.ordinal()] = config.getString(entry.name() + ".syntax", commandLocale.syntax);
                config.set(entry.name() + ".syntax", this.syntax[entry.ordinal()]);
            }

            this.descriptions[entry.ordinal()] = config.getString(entry.name() + ".description", commandLocale.description);
            config.set(entry.name() + ".description", this.descriptions[entry.ordinal()]);

            this.perms[entry.ordinal()] = config.getString(entry.name() + ".perm", commandLocale.perm);
            config.set(entry.name() + ".perm", commandLocale.perm);

            if (commandLocale.aliases != null) {
                this.aliases[entry.ordinal()] = config.getString(entry.name() + ".aliases", commandLocale.aliases);
                config.set(entry.name() + ".aliases", this.aliases[entry.ordinal()]);
            }

        }

        new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir();

        try {
            config.save(Constants.COMMANDS_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private void addDefault(final Commands id, final String name, final String perm, final String syntax, final String description) {
        commandLocales[id.ordinal()] = new CommandLocale(name, syntax, description, perm, null);
    }

    public String getName(Commands id) {
        return name[id.ordinal()];
    }

    public String getSyntax(Commands id) {
        return syntax[id.ordinal()] != null ? syntax[id.ordinal()] : "";
    }

    public String getDescription(Commands id) {
        return descriptions[id.ordinal()];
    }

    public String getPerm(Commands id) {
        return perms[id.ordinal()];
    }

    public String getAliases(Commands id) {
        return aliases[id.ordinal()] != null ? aliases[id.ordinal()] : "";
    }

}
