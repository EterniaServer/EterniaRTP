package br.com.eterniaserver.config;

import br.com.eterniaserver.methods.PlayerCooldown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Checks extends org.bukkit.scheduler.BukkitRunnable {

    private final Configs configs;

    public Checks(Configs configs) {
        this.configs = configs;
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (configs.ptp.containsKey(player)) {
                final PlayerCooldown playerCooldown = configs.ptp.get(player);
                if (playerCooldown.getCountdown() == 0) {
                    configs.ptp.remove(player);
                } else {
                    playerCooldown.decreaseCountdown();
                }
            }
        }
    }

}
