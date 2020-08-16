package br.com.eterniaserver.eterniartp.generic;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.Strings;
import br.com.eterniaserver.paperlib.PaperLib;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandAlias("rtp|wild")
@CommandPermission("eternia.rtp")
public class RTP extends BaseCommand {

    private final Random rand = new Random();

    private final EterniaRTP plugin;
    private final Economy economy;

    public RTP(EterniaRTP plugin) {
        this.plugin = plugin;
        this.economy = plugin.getEcon();


        String query = Constants.getQuerySelectAll(Constants.TABLE_TIME);
        Map<String, String> temp = EQueries.getMapString(query, Strings.UUID, Strings.TIME);
        temp.forEach((k, v) -> Vars.rtp.put(UUID.fromString(k), Long.parseLong(v)));
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_RTP_MODULE.replace(Constants.MODULE, "RTP Times").replace(Constants.AMOUNT, String.valueOf(temp.size())));
    }

    @Default
    public void onRTP(Player player) {
        final double money = EterniaRTP.serverConfig.getDouble("server.amount");
        final boolean econ = EterniaRTP.serverConfig.getBoolean("server.money");
        final int cooldown = EterniaRTP.serverConfig.getInt("server.cooldown");
        final int time = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.rtp.getOrDefault(UUIDFetcher.getUUIDOf(player.getName()), (long) 100));
        if (!(time > cooldown)) {
            player.sendMessage(Strings.MSG_RTP_WAIT.replace(Constants.COOLDOWN, String.valueOf(cooldown - time)));
            return;
        }

        if (econ) {
            if (!economy.has(player, money)) {
                player.sendMessage(Strings.MSG_NO_MONEY);
                return;
            }
            economy.withdrawPlayer(player, money);
        }

        teleportPlayer(player);
    }

    @Subcommand("reload")
    @CommandPermission("eternia.rtp.admin")
    public void onReload(CommandSender sender) {
        plugin.getFiles().loadConfigs();
        plugin.getFiles().loadMessages();
        plugin.getFiles().loadTable();
        sender.sendMessage(Strings.MSG_RTP_RELOAD);
    }

    private void teleportPlayer(Player player) {
        if (EterniaRTP.serverConfig.getStringList("rtp.worlds-banned").contains(player.getWorld().getName())) {
            player.sendMessage(Strings.MSG_WORLD_BANNED);
            return;
        }

        final boolean econ = EterniaRTP.serverConfig.getBoolean("server.money");
        final double money = EterniaRTP.serverConfig.getDouble("server.amount");
        final int x = (int) (EterniaRTP.serverConfig.getInt("rtp.minx") + (EterniaRTP.serverConfig.getInt("rtp.maxx") - EterniaRTP.serverConfig.getInt("rtp.minx")) * rand.nextDouble());
        final int z = (int) (EterniaRTP.serverConfig.getInt("rtp.minz") + (EterniaRTP.serverConfig.getInt("rtp.maxz") - EterniaRTP.serverConfig.getInt("rtp.minz")) * rand.nextDouble());
        player.sendMessage(Strings.MSG_TELEP);
        PaperLib.getChunkAtAsync(player.getWorld(), x, z).thenRun(() -> {
            World world = player.getWorld();
            int y = 110;
            Location location = new Location(world, x, y, z);
            for (y = 110; y >= 50; y--) {
                if (y <= 50) {
                    player.sendMessage(Strings.MSG_NO_SAFE);
                    if (econ) {
                        economy.depositPlayer(player, money);
                        player.sendMessage(Strings.MSG_ECON_GIVE.replace(Constants.MONEY, String.valueOf(money)));
                    }
                    return;
                }
                location.setY(y);
                if (location.getBlock().getType().equals(Material.AIR)) {
                    location.setY(y - 2);
                    if (!location.getBlock().getType().equals(Material.AIR)) {
                        location.setY(y + 1);
                        break;
                    }
                }
                y -= 1;
            }
            if (econ) {
                player.sendMessage(Strings.MSG_SUC_MONEY.replace(Constants.MONEY, String.valueOf(money)));
            } else {
                player.sendMessage(Strings.MSG_SUC_FREE);
            }
            final long time = System.currentTimeMillis();
            PaperLib.teleportAsync(player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            Vars.rtp.put(UUIDFetcher.getUUIDOf(player.getName()), time);
            EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_TIME, Strings.TIME, time, Strings.UUID, UUIDFetcher.getUUIDOf(player.getName()).toString()));
        });
    }

}
