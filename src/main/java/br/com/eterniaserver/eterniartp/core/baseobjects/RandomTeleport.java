package br.com.eterniaserver.eterniartp.core.baseobjects;

import br.com.eterniaserver.eterniartp.EterniaRTP;

import org.bukkit.entity.Player;

public class RandomTeleport extends BaseRandomTeleport {

    private final EterniaRTP plugin;
    private final Player player;

    public RandomTeleport(final EterniaRTP plugin, final Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void execute() {
        teleport(plugin, player, false, null);
    }

}
