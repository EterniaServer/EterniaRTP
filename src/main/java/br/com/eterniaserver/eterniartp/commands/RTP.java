package br.com.eterniaserver.eterniartp.commands;

import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.config.Configs;
import br.com.eterniaserver.eterniartp.config.Strings;

import io.papermc.lib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RTP implements CommandExecutor {

    private final Strings strings;
    private final Configs configs;
    private final EterniaRTP plugin;

    public RTP(Strings strings, Configs configs, EterniaRTP plugin) {
        this.strings = strings;
        this.configs = configs;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && player.hasPermission("eternia.rtp.reload")) {
                configs.reload();
                player.sendMessage(strings.putPrefix("server.reload"));
            } else if (args.length == 0 && player.hasPermission("eternia.rtp")) {
                if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.rtp.getOrDefault(player, (long) 100)) > configs.configs.getInt("server.cooldown")) {
                    if (configs.econ) {
                        if (configs.economy.has(player, configs.configs.getInt("server.amount"))) {
                            configs.economy.withdrawPlayer(player, configs.configs.getInt("server.amount"));
                            teleportPlayer(player);
                        } else {
                            player.sendMessage(strings.putPrefix("econ.no-money"));
                        }
                    } else {
                        teleportPlayer(player);
                    }
                } else {
                    player.sendMessage(strings.putPrefix("rtp.wait").replace("%time%", String.valueOf(configs.configs.getInt("server.cooldown") - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.rtp.get(player)))));
                }
            } else {
                player.sendMessage(strings.putPrefix("server.no-perm"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(strings.putPrefix("server.only-player"));
        }
        return true;
    }

    private void teleportPlayer(Player player) {
        if (!configs.configs.getStringList("worlds-banned").contains(player.getWorld().getName())) {
            final Random rand = new Random();
            final int x = (int) (configs.configs.getInt("rtp.minx") + (configs.configs.getInt("rtp.maxx") - configs.configs.getInt("rtp.minx")) * rand.nextDouble());
            final int z = (int) (configs.configs.getInt("rtp.minz") + (configs.configs.getInt("rtp.maxz") - configs.configs.getInt("rtp.minz")) * rand.nextDouble());
            player.sendMessage(strings.putPrefix("rtp.telep"));
            plugin.rtp.put(player, System.currentTimeMillis());
            PaperLib.getChunkAtAsync(player.getWorld(), x, z).thenRun(() -> {
                World world = player.getWorld();
                int y = 150;
                Location location = new Location(world, x, y, z);
                boolean hasLand = false;
                while (!hasLand) {
                    if (y <= 50) {
                        plugin.rtp.remove(player);
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
                PaperLib.teleportAsync(player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            });
        } else {
            player.sendMessage(strings.putPrefix("rtp.worldb"));
            if (configs.econ) {
                configs.economy.depositPlayer(player, configs.configs.getInt("server.amount"));
                player.sendMessage(strings.putPrefix("econ.give").replace("%money%", String.valueOf(configs.configs.getInt("server.amount"))));
            }
        }
    }

}
