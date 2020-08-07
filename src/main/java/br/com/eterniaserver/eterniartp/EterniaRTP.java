package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniartp.eternialib.Files;
import br.com.eterniaserver.eterniartp.generic.Events;
import br.com.eterniaserver.eterniartp.generic.RTP;
import br.com.eterniaserver.paperlib.PaperLib;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaRTP extends JavaPlugin {

    private EFiles messages;
    private Files files;

    public static final FileConfiguration serverConfig = new YamlConfiguration();
    public static final FileConfiguration msgConfig = new YamlConfiguration();

    private Economy econ;

    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        files = new Files(this);

        files.loadConfigs();
        files.loadMessages();
        files.loadTable();

        messages = new EFiles(msgConfig);

        vault();

        this.getServer().getPluginManager().registerEvents(new Events(), this);
        EterniaLib.getManager().registerCommand(new RTP(this));

    }

    public Files getFiles() {
        return files;
    }

    public EFiles getMessages() {
        return messages;
    }

    public Economy getEcon() {
        return econ;
    }

    public void vault() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                econ = rsp.getProvider();
            }
        }
    }

}
