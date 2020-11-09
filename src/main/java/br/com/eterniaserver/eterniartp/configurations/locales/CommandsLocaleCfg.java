package br.com.eterniaserver.eterniartp.configurations.locales;

import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.enums.Commands;
import br.com.eterniaserver.eterniartp.objetos.CommandLocale;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandsLocaleCfg {

    private final String[] commands;
    private final String[] syntax;
    private final String[] descriptions;
    private final String[] perms;
    private final String[] aliases;

    public CommandsLocaleCfg() {

        this.commands = new String[Commands.values().length];
        this.syntax = new String[Commands.values().length];
        this.descriptions = new String[Commands.values().length];
        this.perms = new String[Commands.values().length];
        this.aliases = new String[Commands.values().length];

        Map<String, CommandLocale> defaults = new HashMap<>();

        this.addDefault(defaults, Commands.RTP, "rtp|wild", "eternia.rtp", " reload", " Teleporte-se para um local randomizado", null);
        this.addDefault(defaults, Commands.RELOAD, "reload", "eternia.rtp.admin", null, " Reinicie as configurações", null);

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_FILE_PATH));

        for (Commands commandsEnum : Commands.values()) {
            CommandLocale commandLocale = defaults.get(commandsEnum.name());

            this.commands[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".name", commandLocale.name);
            config.set(commandsEnum.name() + ".name", this.commands[commandsEnum.ordinal()]);

            if (commandLocale.syntax != null) {
                this.syntax[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".syntax", commandLocale.syntax);
                config.set(commandsEnum.name() + ".syntax", this.syntax[commandsEnum.ordinal()]);
            }

            this.descriptions[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".description", commandLocale.description);
            config.set(commandsEnum.name() + ".description", this.descriptions[commandsEnum.ordinal()]);

            this.perms[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".perm", commandLocale.perm);
            config.set(commandsEnum.name() + ".perm", commandLocale.perm);

            if (commandLocale.aliases != null) {
                this.aliases[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".aliases", commandLocale.aliases);
                config.set(commandsEnum.name() + ".aliases", this.aliases[commandsEnum.ordinal()]);
            }

        }

        new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir();

        try {
            config.save(Constants.COMMANDS_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        defaults.clear();

    }

    private void addDefault(Map<String, CommandLocale> defaults, Commands id, String name, String perm, String syntax, String description, String aliases) {
        CommandLocale commandLocale = new CommandLocale(id, name, syntax, description, perm, aliases);
        defaults.put(id.name(), commandLocale);
    }

    public String getName(Commands id) {
        return commands[id.ordinal()];
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
