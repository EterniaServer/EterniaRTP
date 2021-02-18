package br.com.eterniaserver.eterniartp.core.baseobjects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportTiming {

    private final Player player;
    private final Location firstLocation;
    private final Location wantLocation;
    private int cooldown;

    public TeleportTiming(final Player player, final Location wantLocation, final int cooldown) {
        this.player = player;
        this.firstLocation = player.getLocation();
        this.wantLocation = wantLocation;
        this.cooldown = cooldown;
    }

    public boolean hasMoved() {
        final Location location = player.getLocation();
        return !(firstLocation.getBlockX() == location.getBlockX() && firstLocation.getBlockY() == location.getBlockY() && firstLocation.getBlockZ() == location.getBlockZ());
    }

    public int getCountdown() {
        return cooldown;
    }

    public void decreaseCountdown() {
        cooldown -= 1;
    }

    public Location getWantLocation() {
        return wantLocation;
    }

}
