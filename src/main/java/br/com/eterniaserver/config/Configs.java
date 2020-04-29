package br.com.eterniaserver.config;

import br.com.eterniaserver.EterniaRTP;

import br.com.eterniaserver.methods.PlayerCooldown;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configs {

    public static final HashMap<Player, PlayerCooldown> ptp = new HashMap<>();

    public static final List<String> cantp = new ArrayList<>();

    public static boolean econ = false;

    public static Economy economy;

    public static FileConfiguration configs;
    public static FileConfiguration messages;

    public Configs(EterniaRTP plugin) {

        File configsConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configsConfigFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        configs = new YamlConfiguration();
        try {
            configs.load(configsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = new YamlConfiguration();
        try {
            messages.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }


}
