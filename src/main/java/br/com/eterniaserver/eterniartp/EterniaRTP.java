package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eterniartp.commands.RTP;
import br.com.eterniaserver.eterniartp.config.Configs;
import br.com.eterniaserver.eterniartp.config.Strings;
import br.com.eterniaserver.eterniartp.dependencies.VaultHook;

import io.papermc.lib.PaperLib;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class EterniaRTP extends JavaPlugin {

    private final Configs configs = new Configs(this);
    private final Strings strings = new Strings(configs);
    public final HashMap<Player, Long> rtp = new HashMap<>();

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        vaultHook(this);
        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(new RTP(strings, configs, this));
    }

    private void vaultHook(EterniaRTP plugin) {
        new VaultHook(plugin, configs, strings);
    }


}
