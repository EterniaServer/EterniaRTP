package br.com.eterniaserver.eterniartp.core;

import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.baseobjects.TeleportTiming;
import br.com.eterniaserver.eterniartp.core.enums.Messages;
import br.com.eterniaserver.eterniartp.core.enums.Strings;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Tick extends BukkitRunnable {

    private final EterniaRTP plugin;

    public Tick(final EterniaRTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final TeleportTiming tTiming = plugin.teleportTiming.get(player.getName());
            if (tTiming != null) {
                execute(tTiming, player);
            }
        }
    }

    private void execute(final TeleportTiming tTiming, final Player player) {
        if (tTiming.getCountdown() == 0 || player.hasPermission(plugin.getString(Strings.PERM_TIMINGS_BYPASS))) {
            PaperLib.teleportAsync(player, tTiming.getWantLocation());
            plugin.teleportTiming.remove(player.getName());
            plugin.sendMessage(player, Messages.RTP_TELEPORTED);
            return;
        }

        if (tTiming.hasMoved()) {
            plugin.teleportTiming.remove(player.getName());
            plugin.sendMessage(player, Messages.MOVED);
            return;
        }

        plugin.sendMessage(player, Messages.RTP_TELEPORTING, String.valueOf(tTiming.getCountdown()));
        tTiming.decreaseCountdown();
    }

}
