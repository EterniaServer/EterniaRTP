package br.com.eterniaserver;

import br.com.eterniaserver.commands.RTP;
import br.com.eterniaserver.config.Checks;
import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.dependencies.VaultHook;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class EterniaRTP extends JavaPlugin {
    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        configsEnable(this);
        vaultHook(this);
        checksEnable(this);

        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(new RTP(this));
    }

    private void configsEnable(EterniaRTP plugin) {
        new Configs(plugin);
    }

    private void vaultHook(EterniaRTP plugin) {
        new VaultHook(plugin);
    }

    private void checksEnable(EterniaRTP plugin) {
        new Checks().runTaskTimerAsynchronously(plugin, 20L, 20);
    }

}
