package br.com.eterniaserver.methods;

import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;

import io.papermc.lib.PaperLib;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RunRTP extends org.bukkit.scheduler.BukkitRunnable {

    private final Player player;
    private final int x, z;
    private final Configs configs;
    private final Strings strings;

    public RunRTP(Player player, int x, int z, Configs configs, Strings strings) {
        this.player = player;
        this.x = x;
        this.z = z;
        this.configs = configs;
        this.strings = strings;
    }

    @Override
    public void run(){
        World world = player.getWorld();
        int y = 150;
        Location location = new Location(world, x, y, z);
        boolean hasLand = false;
        while (!hasLand) {
            if (y <= 50) {
                configs.ptp.remove(player);
                player.sendMessage(strings.putPrefix("rtp.no-safe"));
                if (configs.econ) {
                    configs.economy.depositPlayer(player, configs.configs.getInt("server.amount"));
                    player.sendMessage(strings.putPrefix("econ.give").replace("%money%", String.valueOf(configs.configs.getInt("server.amount"))));
                }
                return;
            } else {
                location.setY(y);
                if (location.getBlock().getType().equals(Material.AIR))
                {
                    location.setY(y - 2);
                    if (!location.getBlock().getType().equals(Material.AIR))
                    {
                        location.setY(y + 1);
                        hasLand = true;
                        continue;
                    }
                }
            }
            y -= 1;
        }
        if (configs.econ) {
            player.sendMessage(strings.putPrefix("rtp.suc-money").replace("%money%", String.valueOf(configs.configs.getInt("server.amount"))));
        } else {
            player.sendMessage(strings.putPrefix("rtp.suc-free"));
        }
        PaperLib.teleportAsync(player, location);
    }

}
