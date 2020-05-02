package br.com.eterniaserver;

import br.com.eterniaserver.commands.RTP;
import br.com.eterniaserver.config.Checks;
import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;
import br.com.eterniaserver.dependencies.VaultHook;
import br.com.eterniaserver.methods.RTPPlayer;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class EterniaRTP extends JavaPlugin {

    private final Configs configs = new Configs(this);
    private final Strings strings = new Strings(configs);
    private final RTPPlayer rtpPlayer = new RTPPlayer(configs, strings);

    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        vaultHook(this);
        checksEnable(this);

        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(new RTP(rtpPlayer, strings, configs));
    }

    private void vaultHook(EterniaRTP plugin) {
        new VaultHook(plugin, configs, strings);
    }

    private void checksEnable(EterniaRTP plugin) {
        new Checks(configs).runTaskTimerAsynchronously(plugin, 20L, 20);
    }

}
