package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.core.baseobjects.WorldHeight;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Strings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RtpCfg implements ReloadableConfiguration {

    private final String[] strings;
    private final int[] integers;
    private final double[] doubles;
    private final Set<String> bannedWorlds;
    private final Set<String> unsafeWorlds;
    private final Map<String, WorldHeight> worldHeightMap;

    public RtpCfg(final String[] strings,
                  final int[] integers,
                  final double[] doubles,
                  final Set<String> bannedWorlds,
                  final Set<String> unsafeWorlds,
                  final Map<String, WorldHeight> worldHeightMap) {
        this.strings = strings;
        this.integers = integers;
        this.doubles = doubles;
        this.bannedWorlds = bannedWorlds;
        this.unsafeWorlds = unsafeWorlds;
        this.worldHeightMap = worldHeightMap;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {

        // Load the configurations
        final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.RTP_FILE_PATH));

        strings[Strings.SERVER_PREFIX.ordinal()] = config.getString("server.prefix", "$8[$aE$9R$8]$7 ").replace('$', (char) 0x00A7);
        strings[Strings.PERM_TIMINGS_BYPASS.ordinal()] = config.getString("server.timing-bypass-perm", "eternia.timings.bypass");

        integers[Integers.COOLDOWN.ordinal()] = config.getInt("server.cooldown", 300);
        integers[Integers.MINX.ordinal()] = config.getInt("rtp.min.x", -15000);
        integers[Integers.MINZ.ordinal()] = config.getInt("rtp.min.z", -15000);
        integers[Integers.MAXX.ordinal()] = config.getInt("rtp.max.x", 15000);
        integers[Integers.MAXZ.ordinal()] = config.getInt("rtp.max.z", 15000);

        doubles[Doubles.RTP_COST.ordinal()] = config.getInt("eco.cost", 300);

        final List<String> tempBannedWorlds = config.getStringList("rtp.banned-worlds");
        final List<String> tempUnsafeWorlds = config.getStringList("rtp.unsafe-worlds");

        if (tempBannedWorlds.size() == 0) {
            tempBannedWorlds.add("world_evento");
        }
        bannedWorlds.clear();
        bannedWorlds.addAll(tempBannedWorlds);

        if (tempUnsafeWorlds.size() == 0) {
            tempUnsafeWorlds.add("world_nether");
            tempUnsafeWorlds.add("world_the_end");
        }
        unsafeWorlds.clear();
        unsafeWorlds.addAll(tempUnsafeWorlds);

        final Map<String, WorldHeight> tempWorldHeightMap = new HashMap<>();
        final ConfigurationSection configurationSection = config.getConfigurationSection("world-heights");
        if (configurationSection != null) {
            for (final String key : config.getConfigurationSection("world-heights").getKeys(false)) {
                tempWorldHeightMap.put(key, new WorldHeight(config.getInt("world-heights." + key + ".min", 64), config.getInt("world-heights." + key + ".max", 128)));
            }
        }

        if (tempWorldHeightMap.size() == 0) {
            tempWorldHeightMap.put(Bukkit.getWorlds().get(0).getName(), new WorldHeight(64, 128));
        }
        worldHeightMap.clear();
        worldHeightMap.putAll(tempWorldHeightMap);

        // Save the configurations
        final FileConfiguration outConfig = new YamlConfiguration();

        outConfig.set("server.prefix", strings[Strings.SERVER_PREFIX.ordinal()]);
        outConfig.set("server.timing-bypass-perm", strings[Strings.PERM_TIMINGS_BYPASS.ordinal()]);

        outConfig.set("server.cooldown", integers[Integers.COOLDOWN.ordinal()]);
        outConfig.set("rtp.min.x", integers[Integers.MINX.ordinal()]);
        outConfig.set("rtp.min.z", integers[Integers.MINZ.ordinal()]);
        outConfig.set("rtp.max.x", integers[Integers.MAXX.ordinal()]);
        outConfig.set("rtp.max.z", integers[Integers.MAXZ.ordinal()]);

        outConfig.set("eco.cost", doubles[Doubles.RTP_COST.ordinal()]);

        outConfig.set("rtp.banned-worlds", bannedWorlds);
        outConfig.set("rtp.unsafe-worlds", unsafeWorlds);

        try {
            outConfig.save(Constants.RTP_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void executeCritical() {

    }

}
