package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Entity;
import br.com.eterniaserver.eterniartp.core.RTPTime;
import br.com.eterniaserver.eterniartp.core.RTP;
import br.com.eterniaserver.eterniartp.core.configurations.CommandsLocaleCfg;
import br.com.eterniaserver.eterniartp.core.configurations.ConfigsCfg;
import br.com.eterniaserver.eterniartp.core.configurations.MessageCfg;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;

import br.com.eterniaserver.eterniartp.core.enums.Strings;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class Manager {

    private final EterniaRTP plugin;

    private Economy economy;

    public Manager(EterniaRTP plugin) {
        this.plugin = plugin;

        loadConfigurations();
        loadVault();

        try {
            Entity<RTPTime> rtpTimeEntity = new Entity<>(RTPTime.class);

            EterniaLib.addTableName("%eternia_rtp_table%", plugin.getString(Strings.TABLE_RTP));

            EterniaLib.getDatabase().register(RTPTime.class, rtpTimeEntity);
        } catch (Exception exception) {
            EterniaLib.registerLog("EE-742-RTPTime");
            return;
        }

        List<RTPTime> rtpTimeList = EterniaLib.getDatabase().listAll(RTPTime.class);
        this.plugin.getLogger().log(Level.INFO, "EterniaRTP: {0} times loaded", rtpTimeList.size());

        EterniaLib.getCmdManager().registerCommand(new RTP(plugin, economy));
    }

    private void loadConfigurations() {
        ConfigsCfg configsCfg = new ConfigsCfg(plugin);
        MessageCfg messageCfg = new MessageCfg(plugin);
        CommandsLocaleCfg commandsLocaleCfg = new CommandsLocaleCfg();

        EterniaLib.registerConfiguration("eterniartp", "config", configsCfg);
        EterniaLib.registerConfiguration("eterniartp", "messages", messageCfg);
        EterniaLib.registerConfiguration("eterniartp", "commands", commandsLocaleCfg);

        configsCfg.executeConfig();
        messageCfg.executeConfig();
        commandsLocaleCfg.executeCritical();

        configsCfg.saveConfiguration(true);
        messageCfg.saveConfiguration(true);
        commandsLocaleCfg.saveConfiguration(true);
    }

    private void loadVault() {
        Optional<Plugin> optionalPlugin = Optional.ofNullable(plugin.getServer().getPluginManager().getPlugin("Vault"));
        if (optionalPlugin.isEmpty()) {
            plugin.booleans()[Booleans.ECON.ordinal()] = false;
            return;
        }

        Optional<RegisteredServiceProvider<Economy>> registeredServiceProviderOptional = Optional.ofNullable(
                plugin.getServer().getServicesManager().getRegistration(Economy.class)
        );
        if (registeredServiceProviderOptional.isEmpty()) {
            plugin.booleans()[Booleans.ECON.ordinal()] = false;
            return;
        }

        this.economy = registeredServiceProviderOptional.get().getProvider();
    }
}
