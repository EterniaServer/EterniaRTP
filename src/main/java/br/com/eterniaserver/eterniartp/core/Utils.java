package br.com.eterniaserver.eterniartp.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.commands.AdvancedCommand;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedCategory;
import br.com.eterniaserver.eternialib.commands.enums.AdvancedRules;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Messages;
import br.com.eterniaserver.eterniartp.core.enums.Strings;

import net.kyori.adventure.text.Component;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.sql.Timestamp;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static class TeleportCommand extends AdvancedCommand {

        private final Player sender;
        private final Runnable runnable;

        private boolean aborted = false;
        private int commandTicks = 0;

        public TeleportCommand(Player sender, Runnable runnable) {
            this.sender = sender;
            this.runnable = runnable;
        }

        @Override
        public Player sender() {
            return sender;
        }

        @Override
        public void execute() {
            runnable.run();
        }

        @Override
        public String getTimeMessage() {
            return "";
        }

        @Override
        public void abort(Component component) {
            this.aborted = true;
            sender.sendMessage(component);
        }

        @Override
        public boolean isAborted() {
            return aborted;
        }

        @Override
        public BukkitTask executeAsynchronously() {
            return null;
        }

        @Override
        public AdvancedCategory getCategory() {
            return AdvancedCategory.CONFIRMATION;
        }

        @Override
        public int neededTimeInSeconds() {
            return 15;
        }

        @Override
        public int getCommandTicks() {
            return commandTicks;
        }

        @Override
        public void addCommandTicks(int i) {
            commandTicks += i;
        }

        @Override
        public AdvancedRules[] getAdvancedRules() {
            return new AdvancedRules[0];
        }
    }

    public static class RTPCommand extends AdvancedCommand {

        private final EterniaRTP plugin;

        private final String message;
        private final Player sender;
        private final Runnable runnable;

        private boolean aborted = false;
        private int commandTicks = 0;

        public RTPCommand(EterniaRTP plugin, Player sender, Runnable runnable) {
            this.plugin = plugin;
            this.sender = sender;
            this.runnable = runnable;
            this.message = EterniaLib.getChatCommons().getMessage(Messages.RTP_TELEPORTING);
        }

        public static void addTeleport(EterniaRTP plugin, Player player, Economy economy) {
            if (player.hasPermission(plugin.getString(Strings.PERM_TIMINGS_BYPASS))) {
                teleportToLocation(plugin, player, economy, 3).run();
                return;
            }

            Utils.RTPCommand teleportCommand = new Utils.RTPCommand(
                    plugin,
                    player,
                    teleportToLocation(plugin, player, economy, 3)
            );

            if (!EterniaLib.getAdvancedCmdManager().addTimedCommand(teleportCommand)) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.RTP_ALREADY_IN_TIMING);
            }
        }


        private static Runnable teleportToLocation(EterniaRTP plugin, Player sender, Economy economy, int tries) {
            return () -> {
                boolean economyEnable = plugin.getBoolean(Booleans.ECON);
                Messages failedMessage = economyEnable ? Messages.RTP_NO_SAFE_REFUND : Messages.RTP_NO_SAFE;

                if (tries == 0) {
                    EterniaLib.getChatCommons().sendMessage(sender, failedMessage);
                    return;
                }

                World world = sender.getWorld();
                String worldName = world.getName();
                WorldConfig worldConfig = plugin.getWorldHeight(worldName);

                BukkitScheduler scheduler = plugin.getServer().getScheduler();

                Block block = getBlockAt(world, worldConfig);
                if (!block.isSolid()) {
                    scheduler.scheduleSyncDelayedTask(plugin, () -> teleportToLocation(plugin, sender, economy, tries - 1).run());
                    return;
                }

                Location location = new Location(world, block.getX(), block.getY() + 2, block.getZ());
                if (economyEnable) {
                    economy.withdrawPlayer(sender, plugin.getDouble(Doubles.RTP_COST));
                }

                sender.teleportAsync(location);

                scheduler.runTaskAsynchronously(plugin, () -> {
                    RTPTime rtpTime = EterniaLib.getDatabase().get(RTPTime.class, sender.getUniqueId());
                    rtpTime.setLastRTP(new Timestamp(System.currentTimeMillis()));
                    EterniaLib.getDatabase().update(RTPTime.class, rtpTime);
                });
            };
        }

        private static Block getBlockAt(World world, WorldConfig worldConfig) {
            int x = (int) (worldConfig.minX() + worldConfig.maxX() - (worldConfig.minX() * Math.random()));
            int z = (int) (worldConfig.minZ() + worldConfig.maxZ() - (worldConfig.minZ() * Math.random()));

            if (world.getEnvironment() == World.Environment.NETHER) {
                Block block = world.getBlockAt(x, worldConfig.minY(), z);

                for (int y = worldConfig.minY() + 1; !block.isSolid() && y <= worldConfig.maxY(); y += 1) {
                    block = world.getBlockAt(x, y, z);
                }

                return block;
            }

            return world.getHighestBlockAt(x, z);
        }

        @Override
        public Player sender() {
            return sender;
        }

        @Override
        public void execute() {
            runnable.run();
        }

        @Override
        public String getTimeMessage() {
            return message;
        }

        @Override
        public void abort(Component component) {
            this.aborted = true;
            sender.sendMessage(component);
        }

        @Override
        public boolean isAborted() {
            return aborted;
        }

        @Override
        public BukkitTask executeAsynchronously() {
            return null;
        }

        @Override
        public AdvancedCategory getCategory() {
            return AdvancedCategory.TIMED;
        }

        @Override
        public int neededTimeInSeconds() {
            return plugin.getInteger(Integers.TELEPORT_DELAY);
        }

        @Override
        public int getCommandTicks() {
            return commandTicks;
        }

        @Override
        public void addCommandTicks(int i) {
            commandTicks += i;
        }

        @Override
        public AdvancedRules[] getAdvancedRules() {
            return new AdvancedRules[] {
                    AdvancedRules.NOT_ATTACK,
                    AdvancedRules.NOT_MOVE,
                    AdvancedRules.NOT_JUMP,
                    AdvancedRules.NOT_BREAK_BLOCK
            };
        }
    }

}
