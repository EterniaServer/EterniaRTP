package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.core.queries.CreateTable;
import br.com.eterniaserver.eternialib.core.queries.Select;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.core.enums.Booleans;
import br.com.eterniaserver.eterniartp.core.enums.Integers;
import br.com.eterniaserver.eterniartp.core.enums.Strings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class ConfigsCfg implements ReloadableConfiguration {

    private final String[] strings;
    private final int[] integers;
    private final boolean[] booleans;
    private final Map<UUID, Long> rtp;

    public ConfigsCfg(final String[] strings,
                      final int[] integers,
                      final boolean[] booleans,
                      final Map<UUID, Long> rtp) {
        this.strings = strings;
        this.integers = integers;
        this.booleans = booleans;
        this.rtp = rtp;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.WARNING_ADVICE;
    }

    @Override
    public void executeConfig() {

        // Load the configurations
        final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.CONFIG_FILE_PATH));

        strings[Strings.TABLE_RTP.ordinal()] = config.getString("server.table-rtp", "er_rtp");
        integers[Integers.SERVER_TICK.ordinal()] = config.getInt("server.tick", 1);
        booleans[Booleans.ECON.ordinal()] = config.getBoolean("eco.enabled", true);

        // Save the configurations
        final FileConfiguration outConfig = new YamlConfiguration();

        outConfig.set("server.table-rtp", strings[Strings.TABLE_RTP.ordinal()]);
        outConfig.set("server.tick", integers[Integers.SERVER_TICK.ordinal()]);
        outConfig.set("eco.enabled", booleans[Booleans.ECON.ordinal()]);

        try {
            outConfig.save(Constants.CONFIG_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void executeCritical() {
        final CreateTable createTable;
        if (EterniaLib.getMySQL()) {
            createTable = new CreateTable(strings[Strings.TABLE_RTP.ordinal()]);
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "time BIGINT(20)");
        } else {
            createTable = new CreateTable(strings[Strings.TABLE_RTP.ordinal()]);
            createTable.columns.set("uuid VARCHAR(36)", "time INTEGER");
        }
        SQL.execute(createTable);

        try (final Connection connection = SQL.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(new Select(strings[Strings.TABLE_RTP.ordinal()]).queryString());
             final ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                rtp.put(UUID.fromString(resultSet.getString("uuid")), resultSet.getLong("time"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
