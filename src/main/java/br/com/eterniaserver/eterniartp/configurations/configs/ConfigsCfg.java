package br.com.eterniaserver.eterniartp.configurations.configs;

import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.enums.ConfigBooleans;
import br.com.eterniaserver.eterniartp.enums.ConfigIntegers;
import br.com.eterniaserver.eterniartp.enums.ConfigStrings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigsCfg {

    public ConfigsCfg(String[] strings, Integer[] integers, Boolean[] booleans, List<String> bannedWorlds) {

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));
        FileConfiguration outConfig = new YamlConfiguration();

        strings[ConfigStrings.SERVER_PREFIX.ordinal()] = config.getString("server.prefix", "$8[$aE$9R$8]$7 ").replace('$', (char) 0x00A7);
        strings[ConfigStrings.TABLE_RTP.ordinal()] = config.getString("server.table-rtp", "er_rtp");

        integers[ConfigIntegers.AMOUNT.ordinal()] = config.getInt("eco.cost", 300);
        integers[ConfigIntegers.COOLDOWN.ordinal()] = config.getInt("server.cooldown", 300);
        integers[ConfigIntegers.MINX.ordinal()] = config.getInt("rtp.min.x", -15000);
        integers[ConfigIntegers.MINZ.ordinal()] = config.getInt("rtp.min.z", -15000);
        integers[ConfigIntegers.MAXX.ordinal()] = config.getInt("rtp.max.x", 15000);
        integers[ConfigIntegers.MAXZ.ordinal()] = config.getInt("rtp.max.z", 15000);

        bannedWorlds = config.getStringList("rtp.banned-worlds");

        if (bannedWorlds.isEmpty()) {
            bannedWorlds.add("world_evento");
            bannedWorlds.add("world_the_end");
            bannedWorlds.add("world_nether");
        }

        booleans[ConfigBooleans.ECON.ordinal()] = config.getBoolean("eco.enabled", true);

        outConfig.set("server.prefix", strings[ConfigStrings.SERVER_PREFIX.ordinal()]);
        outConfig.set("server.table-rtp", strings[ConfigStrings.TABLE_RTP.ordinal()]);

        outConfig.set("eco.cost", integers[ConfigIntegers.AMOUNT.ordinal()]);
        outConfig.set("server.cooldown", integers[ConfigIntegers.COOLDOWN.ordinal()]);
        outConfig.set("rtp.min.x", integers[ConfigIntegers.MINX.ordinal()]);
        outConfig.set("rtp.min.z", integers[ConfigIntegers.MINZ.ordinal()]);
        outConfig.set("rtp.max.x", integers[ConfigIntegers.MAXX.ordinal()]);
        outConfig.set("rtp.max.z", integers[ConfigIntegers.MAXZ.ordinal()]);

        outConfig.set("eco.enabled", booleans[ConfigBooleans.ECON.ordinal()]);

        outConfig.set("rtp.banned-worlds", bannedWorlds);

        try {
            outConfig.save(Constants.CONFIG_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

}
