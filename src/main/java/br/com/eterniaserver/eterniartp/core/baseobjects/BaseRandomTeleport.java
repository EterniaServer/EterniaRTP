package br.com.eterniaserver.eterniartp.core.baseobjects;

import br.com.eterniaserver.eternialib.core.interfaces.CommandConfirmable;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Messages;
import br.com.eterniaserver.paperlib.PaperLib;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class BaseRandomTeleport implements CommandConfirmable {

    public void teleport(final EterniaRTP plugin, final Player player, final boolean isPay, final Economy economy) {
        final int x = (int) (plugin.getInteger(Integers.MINX) + plugin.getInteger(Integers.MAXX) - (plugin.getInteger(Integers.MINX) * Math.random()));
        final int z = (int) (plugin.getInteger(Integers.MINZ) + plugin.getInteger(Integers.MAXZ) - (plugin.getInteger(Integers.MINZ) * Math.random()));

        final World world = player.getWorld();
        PaperLib.getChunkAtAsync(world, x, z).thenRun(() -> {
            final WorldHeight worldHeight = plugin.getWorldHeight(world.getName());
            final Location location = new Location(world, x, worldHeight.getMin(), z);

            for (int y = worldHeight.getMin(); y <= worldHeight.getMax(); y += 2) {
                if (y > worldHeight.getMax()) {
                    if (isPay) {
                        plugin.sendMessage(player, Messages.RTP_NO_SAFE_REFUND);
                    } else {
                        plugin.sendMessage(player, Messages.RTP_NO_SAFE);
                    }
                    return;
                }

                location.setY(y);
                if (location.getBlock().getType().equals(Material.AIR)) {
                    location.setY(y - 2);
                    if (!location.getBlock().getType().equals(Material.AIR)) {
                        location.setY(y + 2);
                        break;
                    }
                }
            }

            plugin.teleportTiming.put(player.getName(), new TeleportTiming(player, location, plugin.getInteger(Integers.COOLDOWN)));
            plugin.putCooldown(player.getUniqueId(), System.currentTimeMillis());
            if (isPay) {
                economy.withdrawPlayer(player, plugin.getDouble(Doubles.RTP_COST));
            }
        });
    }
}
