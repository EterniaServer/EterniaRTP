package br.com.eterniaserver.methods;

import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;
import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class RTPPlayer {
    public static void teleportPlayer(final Player player) {
        if (!Configs.configs.getStringList("worlds-banned").contains(player.getWorld().getName())) {
            final Random rand = new Random();
            double x, y = 170, z;
            x = Configs.configs.getInt("rtp.minx") + (Configs.configs.getInt("rtp.maxx") - Configs.configs.getInt("rtp.minx")) * rand.nextDouble();
            z = Configs.configs.getInt("rtp.minz") + (Configs.configs.getInt("rtp.maxz") - Configs.configs.getInt("rtp.minz")) * rand.nextDouble();
            final Location location = new Location(player.getWorld(), x, y, z);
            boolean hasLand = false;
            while (!hasLand) {
                if (y <= 8) {
                    player.sendMessage(Strings.putPrefix("rtp.no-safe"));
                    if (Configs.econ) {
                        Configs.economy.depositPlayer(player, Configs.configs.getInt("server.amount"));
                        player.sendMessage(Strings.putPrefix("econ.give").replace("%money%", String.valueOf(Configs.configs.getInt("server.amount"))));
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
            if (Configs.econ) {
                player.sendMessage(Strings.putPrefix("rtp.suc-money").replace("%money%", String.valueOf(Configs.configs.getInt("server.amount"))));
            } else {
                player.sendMessage(Strings.putPrefix("rtp.suc-free"));
            }
            PaperLib.teleportAsync(player, location);
            Configs.cantp.add(player.getName());
            Configs.ptp.put(player, new PlayerCooldown());
        } else {
            player.sendMessage(Strings.putPrefix("rtp.worldb"));
            if (Configs.econ) {
                Configs.economy.depositPlayer(player, Configs.configs.getInt("server.amount"));
                player.sendMessage(Strings.putPrefix("econ.give").replace("%money%", String.valueOf(Configs.configs.getInt("server.amount"))));
            }
        }
    }
}
