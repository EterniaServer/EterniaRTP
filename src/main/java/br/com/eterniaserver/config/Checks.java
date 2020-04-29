package br.com.eterniaserver.config;

import br.com.eterniaserver.methods.PlayerCooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Checks extends org.bukkit.scheduler.BukkitRunnable {

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Configs.ptp.containsKey(player)) {
                final PlayerCooldown playerCooldown = Configs.ptp.get(player);
                if (playerCooldown.getCountdown() == 0) {
                    Configs.cantp.remove(player.getName());
                    Configs.ptp.remove(player);
                } else {
                    playerCooldown.decreaseCountdown();
                }
            }
        }
    }

}
