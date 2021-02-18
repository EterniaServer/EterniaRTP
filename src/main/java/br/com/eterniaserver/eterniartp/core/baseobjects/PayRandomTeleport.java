package br.com.eterniaserver.eterniartp.core.baseobjects;

import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Messages;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

public class PayRandomTeleport extends BaseRandomTeleport {

    private final EterniaRTP plugin;
    private final Player player;
    private final Economy economy;

    public PayRandomTeleport(final EterniaRTP plugin, final Player player, final Economy economy) {
        this.plugin = plugin;
        this.player = player;
        this.economy = economy;
    }

    @Override
    public void execute() {
        final double cost = plugin.getDouble(Doubles.RTP_COST);
        if (!economy.has(player, cost)) {
            plugin.sendMessage(player, Messages.ECO_NO_MONEY, String.valueOf(cost));
            return;
        }

        teleport(plugin, player, true, economy);
    }

}
