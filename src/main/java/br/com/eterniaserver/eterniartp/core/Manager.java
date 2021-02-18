package br.com.eterniaserver.eterniartp.core;

import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniartp.core.commands.RTP;
import br.com.eterniaserver.eterniartp.core.configurations.CommandsLocaleCfg;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.EterniaRTP;

import br.com.eterniaserver.eterniartp.core.enums.Commands;
import br.com.eterniaserver.eterniartp.core.enums.Integers;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;

public class Manager {

    private final EterniaRTP plugin;
    private final boolean[] booleans;

    private Economy economy;

    public Manager(final EterniaRTP plugin, final boolean[] booleans) {
        this.plugin = plugin;
        this.booleans = booleans;

        loadVault();
        loadReplacements();
        loadCommands();
        new Tick(plugin).runTaskTimer(plugin, 20L, plugin.getInteger(Integers.SERVER_TICK) * 20L);
    }

    private void loadVault() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            booleans[Booleans.ECON.ordinal()] = false;
            return;
        }

        final RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            booleans[Booleans.ECON.ordinal()] = false;
            return;
        }

        this.economy = rsp.getProvider();
    }

    private void loadReplacements() {
        final CommandsLocaleCfg localeCfg = new CommandsLocaleCfg();
        for (final Commands command : Commands.values()) {
            CommandManager.getCommandReplacements().addReplacements(
                    command.name().toLowerCase(), localeCfg.getName(command),
                    command.name().toLowerCase() + "_description", localeCfg.getDescription(command),
                    command.name().toLowerCase() + "_perm", localeCfg.getPerm(command),
                    command.name().toLowerCase() + "_syntax", localeCfg.getSyntax(command),
                    command.name().toLowerCase() + "_aliases", localeCfg.getAliases(command)
            );
        }
    }

    private void loadCommands() {
        CommandManager.registerCommand(new RTP(plugin, economy));
    }

}
