package br.com.eterniaserver.eterniartp.eternialib;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniartp.EterniaRTP;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class Files {

    private final EterniaRTP plugin;

    public Files(EterniaRTP plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {

        try {
            EterniaRTP.serverConfig.load(EFiles.fileLoad(plugin, "config.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadMessages() {

        try {
            EterniaRTP.msgConfig.load(EFiles.fileLoad(plugin, "messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadTable() {

        new Tables();

    }

}
