package br.com.eterniaserver.methods;

import br.com.eterniaserver.EterniaRTP;
import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;

import io.papermc.lib.PaperLib;

import org.bukkit.entity.Player;

import java.util.Random;

public class RTPPlayer {

    private final Configs configs;
    private final Strings strings;
    private final EterniaRTP plugin;

    public RTPPlayer(Configs configs, Strings strings, EterniaRTP plugin) {
        this.configs = configs;
        this.strings = strings;
        this.plugin = plugin;
    }

    public void teleportPlayer(Player player) {
        if (!configs.configs.getStringList("worlds-banned").contains(player.getWorld().getName())) {
            final Random rand = new Random();
            int x, z;
            x = (int) (configs.configs.getInt("rtp.minx") + (configs.configs.getInt("rtp.maxx") - configs.configs.getInt("rtp.minx")) * rand.nextDouble());
            z = (int) (configs.configs.getInt("rtp.minz") + (configs.configs.getInt("rtp.maxz") - configs.configs.getInt("rtp.minz")) * rand.nextDouble());
            player.sendMessage(strings.putPrefix("rtp.telep"));
            plugin.rtp.put(player, System.currentTimeMillis());
            PaperLib.getChunkAtAsync(player.getWorld(), x, z).thenRun(new RunRTP(player, x, z, configs, strings, plugin));
        } else {
            player.sendMessage(strings.putPrefix("rtp.worldb"));
            if (configs.econ) {
                configs.economy.depositPlayer(player, configs.configs.getInt("server.amount"));
                player.sendMessage(strings.putPrefix("econ.give").replace("%money%", String.valueOf(configs.configs.getInt("server.amount"))));
            }
        }
    }

}
