package br.com.eterniaserver.eterniartp.config;

import br.com.eterniaserver.eterniartp.EterniaRTP;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configs {

    public boolean econ = false;

    public Economy economy;

    public FileConfiguration configs, messages;
    private final EterniaRTP plugin;

    public Configs(EterniaRTP plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        File configsConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (!messagesConfigFile.exists()) plugin.saveResource("messages.yml", false);
        if (!configsConfigFile.exists()) plugin.saveResource("config.yml", false);
        
        messages = new YamlConfiguration();
        configs = new YamlConfiguration();
        try {
            messages.load(messagesConfigFile);
            configs.load(configsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }


}
