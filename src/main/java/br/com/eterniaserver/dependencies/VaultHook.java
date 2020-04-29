package br.com.eterniaserver.dependencies;

import br.com.eterniaserver.EterniaRTP;
import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    public VaultHook(EterniaRTP plugin) {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                if (Configs.configs.getBoolean("server.money")) {
                    Configs.economy = rsp.getProvider();
                    Configs.econ = true;
                } else {
                    Bukkit.getConsoleSender().sendMessage(Strings.putPrefix("econ.disable"));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(Strings.putPrefix("econ.no-hook"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(Strings.putPrefix("econ.vault-no"));
        }
    }

}
