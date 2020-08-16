package br.com.eterniaserver.eterniartp.eternialib;

import br.com.eterniaserver.eterniartp.EterniaRTP;

import br.com.eterniaserver.eterniartp.Strings;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

public class Files {

    private final EterniaRTP plugin;

    public Files(EterniaRTP plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {

        final String config = "config.yml";

        final File file = new File(plugin.getDataFolder(), config);
        if (!file.exists()) plugin.saveResource(config, false);

        try {
            EterniaRTP.serverConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadMessages() {

        final String messages = "messages.yml";

        final File file = new File(plugin.getDataFolder(), messages);
        if (!file.exists()) plugin.saveResource(messages, false);


        try {
            EterniaRTP.msgConfig.load(file);
            Strings.reloadMessages(EterniaRTP.msgConfig);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadTable() {

        new Tables();

    }

}
