package br.com.eterniaserver.eterniartp.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.APIRTP;
import br.com.eterniaserver.eterniartp.enums.ConfigBooleans;
import br.com.eterniaserver.eterniartp.enums.ConfigIntegers;
import br.com.eterniaserver.eterniartp.enums.Messages;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@CommandAlias("%rtp")
public class RTP extends BaseCommand {

    private final Random rand = new Random();

    @Default
    @Description("%rtp_description")
    @Syntax("%rtp_syntax")
    @CommandCompletion("reload")
    @CommandPermission("%rtp_perm")
    public void onRTP(Player player) {
        final int time = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - APIRTP.getOrDefault(UUIDFetcher.getUUIDOf(player.getName())));
        if (!(time > EterniaRTP.getInteger(ConfigIntegers.COOLDOWN))) {
            EterniaRTP.sendMessage(player, Messages.WAIT, String.valueOf(EterniaRTP.getInteger(ConfigIntegers.COOLDOWN) - time));
            return;
        }

        if (EterniaRTP.getBoolean(ConfigBooleans.ECON)) {
            if (!EterniaRTP.getEcon().has(player, EterniaRTP.getInteger(ConfigIntegers.AMOUNT))) {
                EterniaRTP.sendMessage(player, Messages.NO_MONEY);
                return;
            }
            EterniaRTP.getEcon().withdrawPlayer(player, EterniaRTP.getInteger(ConfigIntegers.AMOUNT));
        }

        teleportPlayer(player);
    }

    @Subcommand("%rtp_reload")
    @Description("%rtp_reload_description")
    @CommandPermission("%rtp_reload_perm")
    public void onReload(CommandSender sender) {
        EterniaRTP.sendMessage(sender, Messages.RTP_RELOAD);
    }

    private void teleportPlayer(Player player) {
        if (EterniaRTP.getBannedWorlds().contains(player.getWorld().getName())) {
            EterniaRTP.sendMessage(player, Messages.BANNED_WORLD);
            return;
        }

        final int x = (int) (EterniaRTP.getInteger(ConfigIntegers.MINX) + EterniaRTP.getInteger(ConfigIntegers.MAXX) - (EterniaRTP.getInteger(ConfigIntegers.MINX) * rand.nextDouble()));
        final int z = (int) (EterniaRTP.getInteger(ConfigIntegers.MINZ) + EterniaRTP.getInteger(ConfigIntegers.MAXZ) - (EterniaRTP.getInteger(ConfigIntegers.MINZ) * rand.nextDouble()));
        EterniaRTP.sendMessage(player, Messages.RTP_TELEPORT);

        PaperLib.getChunkAtAsync(player.getWorld(), x, z).thenRun(() -> {
            World world = player.getWorld();
            int y = 110;
            Location location = new Location(world, x, y, z);
            for (y = 110; y >= 50; y--) {
                if (y <= 50) {
                    EterniaRTP.sendMessage(player, Messages.NOT_SAFE);
                    if (EterniaRTP.getBoolean(ConfigBooleans.ECON)) {
                        EterniaRTP.getEcon().depositPlayer(player, EterniaRTP.getInteger(ConfigIntegers.AMOUNT));
                        EterniaRTP.sendMessage(player, Messages.REFUND, String.valueOf(EterniaRTP.getInteger(ConfigIntegers.AMOUNT)));
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
            if (EterniaRTP.getBoolean(ConfigBooleans.ECON)) {
                EterniaRTP.sendMessage(player, Messages.TELEPORTED, String.valueOf(EterniaRTP.getInteger(ConfigIntegers.AMOUNT)));
            } else {
                EterniaRTP.sendMessage(player, Messages.TELEPORTED_FREE);
            }
            final long time = System.currentTimeMillis();
            PaperLib.teleportAsync(player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            APIRTP.put(UUIDFetcher.getUUIDOf(player.getName()), time);
        });
    }

}
