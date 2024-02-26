package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eterniartp.core.WorldConfig;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Strings;

import org.bstats.bukkit.Metrics;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EterniaRTP extends JavaPlugin {

    private final WorldConfig DEFAULT_WORLD_CONFIG = new WorldConfig(
            64, 192, -15000, 15000, -15000, 15000
    );

    private final Map<String, WorldConfig> worldConfigMap = new HashMap<>();

    private final EnumMap<Strings, String> strings = new EnumMap<>(Strings.class);
    private final EnumMap<Integers, Integer> integers = new EnumMap<>(Integers.class);
    private final EnumMap<Doubles, Double> doubles = new EnumMap<>(Doubles.class);
    private final EnumMap<Booleans, Boolean> booleans = new EnumMap<>(Booleans.class);

    private final Set<String> bannedWorlds = new HashSet<>();
    private final Set<String> unsafeWorlds = new HashSet<>();

    @Override
    public void onEnable() {
        new Metrics(this, 8446);
        new Manager(this);
    }

    public WorldConfig getWorldHeight(String worldName) {
        return worldConfigMap.getOrDefault(worldName, DEFAULT_WORLD_CONFIG);
    }

    public String getString(Strings entry) {
        return strings.get(entry);
    }

    public int getInteger(Integers entry) {
        return integers.get(entry);
    }

    public double getDouble(Doubles entry) {
        return doubles.get(entry);
    }

    public boolean getBoolean(Booleans entry) {
        return booleans.get(entry);
    }

    public boolean worldIsBanned(String worldName) {
        return bannedWorlds.contains(worldName);
    }

    public boolean worldIsUnsafe(String worldName) {
        return unsafeWorlds.contains(worldName);
    }

    public EnumMap<Strings, String> strings() {
        return strings;
    }

    public EnumMap<Integers, Integer> integers() {
        return integers;
    }

    public EnumMap<Doubles, Double> doubles() {
        return doubles;
    }

    public EnumMap<Booleans, Boolean> booleans() {
        return booleans;
    }

    public Map<String, WorldConfig> worldConfigMap() {
        return worldConfigMap;
    }

    public Set<String> bannedWorlds() {
        return bannedWorlds;
    }

    public Set<String> unsafeWorlds() {
        return unsafeWorlds;
    }

}
