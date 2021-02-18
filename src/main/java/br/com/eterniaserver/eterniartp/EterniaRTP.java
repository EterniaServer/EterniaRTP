package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Insert;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniartp.core.baseobjects.TeleportTiming;
import br.com.eterniaserver.eterniartp.core.baseobjects.WorldHeight;
import br.com.eterniaserver.eterniartp.core.configurations.ConfigsCfg;
import br.com.eterniaserver.eterniartp.core.configurations.MessageCfg;
import br.com.eterniaserver.eterniartp.core.Manager;
import br.com.eterniaserver.eterniartp.core.configurations.RtpCfg;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Strings;
import br.com.eterniaserver.eterniartp.core.enums.Messages;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bstats.bukkit.Metrics;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EterniaRTP extends JavaPlugin {

    private final Map<UUID, Long> rtp = new HashMap<>();
    private final Map<String, WorldHeight> worldHeightMap = new HashMap<>();

    private final String[] strings = new String[Strings.values().length];
    private final String[] messages = new String[Messages.values().length];
    private final int[] integers = new int[Integers.values().length];
    private final double[] doubles = new double[Doubles.values().length];
    private final boolean[] booleans = new boolean[Booleans.values().length];

    private final Set<String> bannedWorlds = new HashSet<>();
    private final Set<String> unsafeWorlds = new HashSet<>();

    public final Map<String, TeleportTiming> teleportTiming = new HashMap<>();

    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        loadConfigurations();

        new Metrics(this, 8446);
        new Manager(this, booleans);
    }

    private void loadConfigurations() {
        final ConfigsCfg configsCfg = new ConfigsCfg(strings, integers, booleans, rtp);
        final RtpCfg rtpCfg = new RtpCfg(strings, integers, doubles, bannedWorlds, unsafeWorlds, worldHeightMap);
        final MessageCfg messageCfg = new MessageCfg(messages);

        EterniaLib.addReloadableConfiguration("eterniartp", "configs", configsCfg);
        EterniaLib.addReloadableConfiguration("eterniartp", "rtp", rtpCfg);
        EterniaLib.addReloadableConfiguration("eterniartp", "messages", messageCfg);

        configsCfg.executeConfig();
        configsCfg.executeCritical();
        rtpCfg.executeConfig();
        messageCfg.executeConfig();
    }

    public String getString(Strings configName) {
        return strings[configName.ordinal()];
    }

    public int getInteger(Integers configName) {
        return integers[configName.ordinal()];
    }

    public double getDouble(final Doubles entry) {
        return doubles[entry.ordinal()];
    }

    public boolean getBoolean(Booleans configName) {
        return booleans[configName.ordinal()];
    }

    public boolean worldIsBanned(final String worldName) {
        return bannedWorlds.contains(worldName);
    }

    public boolean worldIsUnsafe(final String worldName) {
        return unsafeWorlds.contains(worldName);
    }

    public void sendMessage(final CommandSender sender, final Messages entry, final String... args) {
        sender.sendMessage(getMessage(entry, args));
    }

    public String getMessage(final Messages entry, final String... args) {
        String message = messages[entry.ordinal()];

        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }

        return getString(Strings.SERVER_PREFIX) + message;
    }

    public WorldHeight getWorldHeight(final String worldName) {
        return worldHeightMap.getOrDefault(worldName, new WorldHeight(64, 128));
    }

    public long getCooldown(UUID uuid) {
        return rtp.getOrDefault(uuid, 0L);
    }

    public void putCooldown(UUID uuid, long time) {
        if (!rtp.containsKey(uuid)) {
            final Insert insert = new Insert(strings[Strings.TABLE_RTP.ordinal()]);
            insert.columns.set("uuid", "time");
            insert.values.set(uuid.toString(), time);
            SQL.executeAsync(insert);
            rtp.put(uuid, time);
            return;
        }

        final Update update = new Update(strings[Strings.TABLE_RTP.ordinal()]);
        update.set.set("time", time);
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
        rtp.put(uuid, time);
    }

}
