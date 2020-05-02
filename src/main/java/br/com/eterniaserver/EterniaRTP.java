package br.com.eterniaserver;

import br.com.eterniaserver.commands.RTP;
import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;
import br.com.eterniaserver.dependencies.VaultHook;
import br.com.eterniaserver.methods.RTPPlayer;
import io.papermc.lib.PaperLib;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class EterniaRTP extends JavaPlugin {

    private final Configs configs = new Configs(this);
    private final Strings strings = new Strings(configs);
    private final RTPPlayer rtpPlayer = new RTPPlayer(configs, strings, this);
    public final HashMap<Player, Long> rtp = new HashMap<>();

    @Override
    public void onEnable() {

        PaperLib.suggestPaper(this);

        vaultHook(this);

        Objects.requireNonNull(this.getCommand("rtp")).setExecutor(new RTP(rtpPlayer, strings, configs, this));
    }

    private void vaultHook(EterniaRTP plugin) {
        new VaultHook(plugin, configs, strings);
    }


}
