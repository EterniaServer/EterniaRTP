package br.com.eterniaserver.eterniartp.core.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.CmdConfirmationManager;
import br.com.eterniaserver.eterniartp.EterniaRTP;

import br.com.eterniaserver.eterniartp.core.baseobjects.PayRandomTeleport;
import br.com.eterniaserver.eterniartp.core.baseobjects.RandomTeleport;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@CommandAlias("%rtp")
public class RTP extends BaseCommand {

    private final EterniaRTP plugin;
    private final Economy economy;

    public RTP(final EterniaRTP plugin, final Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    @CatchUnknown
    @HelpCommand
    @Subcommand("%rtp_help")
    @CommandPermission("%rtp_help_perm")
    @Description("%rtp_help_description")
    @Syntax("%rtp_help_syntax")
    public void onRTPHelp(final CommandHelp help) {
        help.showHelp();
    }

    @Default
    @Description("%rtp_description")
    @CommandPermission("%rtp_perm")
    public void onRTP(final Player player) {

        final int time = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - plugin.getCooldown(player.getUniqueId()));
        if (time <= plugin.getInteger(Integers.COOLDOWN)) {
            plugin.sendMessage(player, Messages.PLAYER_IN_COOLDOWN, String.valueOf(plugin.getInteger(Integers.COOLDOWN) - time));
            return;
        }

        final String worldName = player.getWorld().getName();
        if (plugin.worldIsBanned(worldName)) {
            plugin.sendMessage(player, Messages.BANNED_WORLD, worldName);
            return;
        }
        if (plugin.worldIsUnsafe(worldName)) {
            plugin.sendMessage(player, Messages.UNSAFE_WORLD, worldName);
        }

        if (plugin.getBoolean(Booleans.ECON)) {
            final PayRandomTeleport payRandomTeleport = new PayRandomTeleport(plugin, player, economy);
            CmdConfirmationManager.scheduleCommand(player, payRandomTeleport);
            plugin.sendMessage(player, Messages.ECO_PAY_RTP, String.valueOf(plugin.getDouble(Doubles.RTP_COST)));
            return;
        }

        final RandomTeleport randomTeleport = new RandomTeleport(plugin, player);
        CmdConfirmationManager.scheduleCommand(player, randomTeleport);
        plugin.sendMessage(player, Messages.ECO_FREE_RTP);
    }


}
