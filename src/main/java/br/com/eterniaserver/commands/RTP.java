package br.com.eterniaserver.commands;

import br.com.eterniaserver.EterniaRTP;
import br.com.eterniaserver.config.Configs;
import br.com.eterniaserver.config.Strings;
import br.com.eterniaserver.methods.RTPPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RTP implements CommandExecutor {

    private final EterniaRTP plugin;

    public RTP(EterniaRTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.rtp")) {
                if (args.length == 1 && player.hasPermission("eternia.rtp.reload")) {
                    new Configs(plugin);
                    player.sendMessage(Strings.putPrefix("server.reload"));
                } else {
                    if (!Configs.cantp.contains(player.getName())) {
                        if (Configs.econ) {
                            if (Configs.economy.has(player, Configs.configs.getInt("server.amount"))) {
                                Configs.economy.withdrawPlayer(player, Configs.configs.getInt("server.amount"));
                                RTPPlayer.teleportPlayer(player);
                            } else {
                                player.sendMessage(Strings.putPrefix("econ.no-money"));
                            }
                        } else {
                            RTPPlayer.teleportPlayer(player);
                        }
                    } else {
                        player.sendMessage(Strings.putPrefix("rtp.wait"));
                    }
                }
            } else {
                player.sendMessage(Strings.putPrefix("server.no-perm"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(Strings.putPrefix("server.only-player"));
        }
        return true;
    }

}
