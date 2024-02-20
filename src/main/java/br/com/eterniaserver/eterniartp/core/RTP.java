package br.com.eterniaserver.eterniartp.core;

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

import br.com.eterniaserver.eternialib.EterniaLib;

import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Messages;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@CommandAlias("%RTP")
public class RTP extends BaseCommand {

    private final EterniaRTP plugin;
    private final Economy economy;

    public RTP(EterniaRTP plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    @CatchUnknown
    @HelpCommand
    @Subcommand("%RTP_HELP")
    @CommandPermission("%RTP_HELP_PERM")
    @Description("%RTP_HELP_DESCRIPTION")
    @Syntax("%RTP_HELP_SYNTAX")
    public void onRTPHelp(CommandHelp help) {
        help.showHelp();
    }

    @Default
    @Description("%RTP_DESCRIPTION")
    @CommandPermission("%RTP_PERM")
    public void onRTP(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.getBoolean(Booleans.ECON) && !economy.has(player, plugin.getDouble(Doubles.RTP_COST))) {
                plugin.sendMiniMessages(player, Messages.ECO_NO_MONEY, String.valueOf(plugin.getDouble(Doubles.RTP_COST)));
                return;
            }

            RTPTime rtpTime = EterniaLib.getDatabase().get(RTPTime.class, player.getUniqueId());

            int secondsCooldown = plugin.getInteger(Integers.COOLDOWN);

            if (rtpTime.getUuid() == null) {
                rtpTime.setUuid(player.getUniqueId());
                rtpTime.setLastRTP(new Timestamp(
                        System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(secondsCooldown + 10)
                ));
                EterniaLib.getDatabase().insert(RTPTime.class, rtpTime);
            }

            long actualTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            long lastUse = TimeUnit.MILLISECONDS.toSeconds(rtpTime.getLastRTP().getTime());
            int cooldown = (int) (actualTime - lastUse);

            if (cooldown < secondsCooldown) {
                plugin.sendMiniMessages(player, Messages.PLAYER_IN_COOLDOWN, String.valueOf(secondsCooldown - cooldown));
                return;
            }

            String worldName = player.getWorld().getName();
            if (plugin.worldIsBanned(worldName)) {
                plugin.sendMiniMessages(player, Messages.BANNED_WORLD, worldName);
                return;
            }

            if (plugin.worldIsUnsafe(worldName)) {
                plugin.sendMiniMessages(player, Messages.UNSAFE_WORLD, worldName);
            }

            Utils.TeleportCommand teleportCommand = new Utils.TeleportCommand(
                    player, () -> Utils.RTPCommand.addTeleport(plugin, player, economy)
            );

            boolean result = EterniaLib.getAdvancedCmdManager().addConfirmationCommand(teleportCommand);
            if (!result) {
                plugin.sendMiniMessages(player, Messages.RTP_ALREADY_IN_CONFIRMATION);
                return;
            }

            if (!plugin.getBoolean(Booleans.ECON)) {
                plugin.sendMiniMessages(player, Messages.ECO_FREE_RTP);
                return;
            }

            plugin.sendMiniMessages(player, Messages.ECO_PAY_RTP, String.valueOf(plugin.getDouble(Doubles.RTP_COST)));
        });
    }


}
