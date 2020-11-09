package br.com.eterniaserver.eterniartp.events;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.queries.Select;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.APIRTP;

import br.com.eterniaserver.eterniartp.enums.ConfigStrings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerHandler implements Listener {

    public PlayerHandler() {
        try {
            PreparedStatement preparedStatement = SQL.getConnection().prepareStatement(new Select(EterniaRTP.getString(ConfigStrings.TABLE_RTP)).queryString());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                APIRTP.put(UUIDFetcher.getUUIDOf(resultSet.getString("uuid")), resultSet.getLong("time"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        APIRTP.checkAndPut(UUIDFetcher.getUUIDOf(event.getPlayer().getName()));
    }

}
