package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.WorldConfig;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Strings;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ConfigsCfg implements ReloadableConfiguration {

    private final EterniaRTP plugin;

    private final FileConfiguration inFile;
    private final FileConfiguration outFile;

    public ConfigsCfg(EterniaRTP plugin) {
        this.plugin = plugin;

        this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        this.outFile = new YamlConfiguration();
    }

    @Override
    public FileConfiguration inFileConfiguration() {
        return inFile;
    }

    @Override
    public FileConfiguration outFileConfiguration() {
        return outFile;
    }

    @Override
    public String[] messages() {
        return new String[0];
    }

    @Override
    public CommandLocale[] commandsLocale() {
        return new CommandLocale[0];
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public String getFolderPath() {
        return Constants.DATA_LAYER_FOLDER_PATH;
    }

    @Override
    public String getFilePath() {
        return Constants.CONFIG_FILE_PATH;
    }

    @Override
    public void executeConfig() {
        String[] strings = plugin.strings();;
        double[] doubles = plugin.doubles();
        int[] integers = plugin.integers();
        boolean[] booleans = plugin.booleans();

        strings[Strings.TABLE_RTP.ordinal()] = inFile.getString("server.table-rtp", "er_rtp_times");
        strings[Strings.SERVER_PREFIX.ordinal()] = inFile.getString("server.prefix", "<color:#555555>[<color:#34eb40>E<color:#3471eb>R<color:#555555>]<color:#AAAAAA> ");
        strings[Strings.PERM_TIMINGS_BYPASS.ordinal()] = inFile.getString("server.timing-bypass-perm", "eternia.timings.bypass");

        doubles[Doubles.RTP_COST.ordinal()] = inFile.getInt("eco.cost", 50);

        integers[Integers.COOLDOWN.ordinal()] = inFile.getInt("server.cooldown", 300);
        integers[Integers.TELEPORT_DELAY.ordinal()] = inFile.getInt("teleport-delay", 5);

        booleans[Booleans.ECON.ordinal()] = inFile.getBoolean("eco.enabled", true);

        List<String> tempBannedWorlds = inFile.getStringList("rtp.banned-worlds");
        List<String> tempUnsafeWorlds = inFile.getStringList("rtp.unsafe-worlds");

        if (tempBannedWorlds.isEmpty()) {
            tempBannedWorlds.add("world_evento");
        }
        plugin.bannedWorlds().clear();
        plugin.bannedWorlds().addAll(tempBannedWorlds);

        if (tempUnsafeWorlds.isEmpty()) {
            tempUnsafeWorlds.add("world_nether");
            tempUnsafeWorlds.add("world_the_end");
        }
        plugin.unsafeWorlds().clear();
        plugin.unsafeWorlds().addAll(tempUnsafeWorlds);

        plugin.worldConfigMap().clear();
        ConfigurationSection configurationSection = inFile.getConfigurationSection("worlds-configs");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                plugin.worldConfigMap().put(
                        key,
                        new WorldConfig(
                                inFile.getInt("worlds-configs." + key + ".min.y", 64),
                                inFile.getInt("worlds-configs." + key + ".max.y", 196),
                                inFile.getInt("worlds-configs." + key + ".min.x", -15000),
                                inFile.getInt("worlds-configs." + key + ".max.x", 15000),
                                inFile.getInt("worlds-configs." + key + ".min.z", -15000),
                                inFile.getInt("worlds-configs." + key + ".max.z", 15000)
                        )
                );
            }
        }

        for (Map.Entry<String, WorldConfig> entry : plugin.worldConfigMap().entrySet()) {
            outFile.set("worlds-configs." + entry.getKey() + ".min.y", entry.getValue().minY());
            outFile.set("worlds-configs." + entry.getKey() + ".max.y", entry.getValue().maxY());
            outFile.set("worlds-configs." + entry.getKey() + ".min.x", entry.getValue().minX());
            outFile.set("worlds-configs." + entry.getKey() + ".max.x", entry.getValue().maxX());
            outFile.set("worlds-configs." + entry.getKey() + ".min.z", entry.getValue().minZ());
            outFile.set("worlds-configs." + entry.getKey() + ".max.z", entry.getValue().maxZ());
        }

        outFile.set("eco.cost", doubles[Doubles.RTP_COST.ordinal()]);

        outFile.set("rtp.banned-worlds", plugin.bannedWorlds().toArray());
        outFile.set("rtp.unsafe-worlds", plugin.unsafeWorlds().toArray());

        outFile.set("server.table-rtp", strings[Strings.TABLE_RTP.ordinal()]);
        outFile.set("server.prefix", strings[Strings.SERVER_PREFIX.ordinal()]);
        outFile.set("server.timing-bypass-perm", strings[Strings.PERM_TIMINGS_BYPASS.ordinal()]);

        outFile.set("eco.enabled", booleans[Booleans.ECON.ordinal()]);

        outFile.set("server.cooldown", integers[Integers.COOLDOWN.ordinal()]);
        outFile.set("teleport-delay", integers[Integers.TELEPORT_DELAY.ordinal()]);
    }

    @Override
    public void executeCritical() { }
}
