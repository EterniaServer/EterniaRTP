package br.com.eterniaserver.eterniartp.events;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Select;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.APIRTP;
import br.com.eterniaserver.eterniartp.enums.ConfigStrings;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class PlayerHandler implements Listener {

    public PlayerHandler() {
        try (Connection connection = SQL.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(new Select(EterniaRTP.getString(ConfigStrings.TABLE_RTP)).queryString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                APIRTP.putWithoutUpdate(UUID.fromString(resultSet.getString("uuid")), resultSet.getLong("time"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        APIRTP.checkAndPut(event.getPlayer().getUniqueId());
    }

}
