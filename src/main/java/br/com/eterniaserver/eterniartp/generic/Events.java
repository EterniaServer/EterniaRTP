package br.com.eterniaserver.eterniartp.generic;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniartp.Constants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class Events implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final String playerName = event.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!Vars.rtp.containsKey(uuid)) {
            final long time = System.currentTimeMillis();
            EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_TIME, "(uuid, time)", "('" + uuid.toString() + "', '" + time + "')"), false);
            Vars.rtp.put(uuid, time);
        }
    }

}
