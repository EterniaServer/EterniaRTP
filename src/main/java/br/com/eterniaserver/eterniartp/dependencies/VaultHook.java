package br.com.eterniaserver.eterniartp.dependencies;

import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.config.Configs;
import br.com.eterniaserver.eterniartp.config.Strings;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    public VaultHook(EterniaRTP plugin, Configs configs, Strings strings) {
        if (configs.configs.getBoolean("server.money")) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
                if (rsp != null) {
                    configs.economy = rsp.getProvider();
                    configs.econ = true;
                } else {
                    Bukkit.getConsoleSender().sendMessage(strings.putPrefix("econ.no-hook"));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(strings.putPrefix("econ.vault-no"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(strings.putPrefix("econ.disable"));
        }
    }

}
