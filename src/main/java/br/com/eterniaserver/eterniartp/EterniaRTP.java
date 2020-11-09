package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniartp.commands.RTP;
import br.com.eterniaserver.eterniartp.configurations.configs.ConfigsCfg;
import br.com.eterniaserver.eterniartp.configurations.configs.TableCfg;
import br.com.eterniaserver.eterniartp.configurations.locales.CommandsLocaleCfg;
import br.com.eterniaserver.eterniartp.configurations.locales.MsgCfg;
import br.com.eterniaserver.eterniartp.enums.Commands;
import br.com.eterniaserver.eterniartp.enums.ConfigBooleans;
import br.com.eterniaserver.eterniartp.enums.ConfigIntegers;
import br.com.eterniaserver.eterniartp.events.PlayerHandler;
import br.com.eterniaserver.eterniartp.enums.ConfigStrings;
import br.com.eterniaserver.eterniartp.enums.Messages;
import br.com.eterniaserver.paperlib.PaperLib;

import net.milkbowl.vault.economy.Economy;

import org.bstats.bukkit.Metrics;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EterniaRTP extends JavaPlugin {

    private static final String[] strings = new String[ConfigStrings.values().length];
    private static final String[] messages = new String[Messages.values().length];
    private static final Integer[] integers = new Integer[ConfigIntegers.values().length];
    private static final Boolean[] booleans = new Boolean[ConfigBooleans.values().length];

    private static final List<String> bannedWorlds = new ArrayList<>();

    private static Economy econ;

    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                econ = rsp.getProvider();
            }
        }

        loadCommandsLocale();

        new Metrics(this, 8446);

        new ConfigsCfg(strings, integers, booleans, bannedWorlds);
        new MsgCfg(messages);
        new TableCfg();

        CommandManager.registerCommand(new RTP());

        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);

    }

    public static String getString(ConfigStrings configName) {
        return strings[configName.ordinal()];
    }

    public static int getInteger(ConfigIntegers configName) {
        return integers[configName.ordinal()];
    }

    public static boolean getBoolean(ConfigBooleans configName) {
        return booleans[configName.ordinal()];
    }

    public static List<String> getBannedWorlds() {
        return bannedWorlds;
    }

    public static String getMessage(Messages messagesId, boolean prefix, String... args) {
        return Constants.getMessage(messagesId, prefix, messages, args);
    }

    public static void sendMessage(CommandSender sender, Messages messagesId, String... args) {
        Constants.sendMessage(sender, messagesId, args);
    }

    private void loadCommandsLocale() {
        CommandsLocaleCfg cmdsLocale = new CommandsLocaleCfg();
        for (Commands command : Commands.values()) {
            CommandManager.getCommandReplacements().addReplacements(
                    command.name().toLowerCase(), cmdsLocale.getName(command),
                    command.name().toLowerCase() + "_description", cmdsLocale.getDescription(command),
                    command.name().toLowerCase() + "_perm", cmdsLocale.getPerm(command),
                    command.name().toLowerCase() + "_syntax", cmdsLocale.getSyntax(command),
                    command.name().toLowerCase() + "_aliases", cmdsLocale.getAliases(command)
            );
        }
    }

    public static Economy getEcon() {
        return econ;
    }

}
