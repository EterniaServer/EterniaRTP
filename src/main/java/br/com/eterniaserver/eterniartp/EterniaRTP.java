package br.com.eterniaserver.eterniartp;

import br.com.eterniaserver.eterniartp.core.WorldConfig;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Doubles;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Strings;
import br.com.eterniaserver.eterniartp.core.enums.Messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bstats.bukkit.Metrics;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EterniaRTP extends JavaPlugin {

    private final WorldConfig DEFAULT_WORLD_CONFIG = new WorldConfig(
            64, 192, -15000, 15000, -15000, 15000
    );

    private final MiniMessage miniMessage =  MiniMessage.miniMessage();

    private final Map<String, WorldConfig> worldConfigMap = new HashMap<>();

    private final String[] strings = new String[Strings.values().length];
    private final String[] messages = new String[Messages.values().length];
    private final int[] integers = new int[Integers.values().length];
    private final double[] doubles = new double[Doubles.values().length];
    private final boolean[] booleans = new boolean[Booleans.values().length];

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

    public String[] strings() {
        return strings;
    }

    public String[] messages() {
        return messages;
    }

    public int[] integers() {
        return integers;
    }

    public double[] doubles() {
        return doubles;
    }

    public boolean[] booleans() {
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

    public void sendMiniMessages(CommandSender sender, Messages messagesId, String... args) {
        sender.sendMessage(parseColor(strings[Strings.SERVER_PREFIX.ordinal()] + getMessage(messagesId, args)));
    }

    public String getMessage(Messages messagesId, String... args) {
        String message = messages[messagesId.ordinal()];

        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }

        return message;
    }

    public Component parseColor(String string) {
        return miniMessage.deserialize(string);
    }

}
