package br.com.eterniaserver.eterniartp.core;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.enums.ConfigStrings;

import java.util.UUID;

public interface APIRTP {

    static long getOrDefault(UUID uuid) {
        return Vars.rtp.getOrDefault(uuid, 100L);
    }

    static void checkAndPut(UUID uuid) {
        if (!Vars.rtp.containsKey(uuid)) {
            Insert insert = new Insert(EterniaRTP.getString(ConfigStrings.TABLE_RTP));
            insert.columns.set("uuid", "time");
            insert.values.set(uuid.toString(), System.currentTimeMillis());
            SQL.executeAsync(insert);
            put(uuid, System.currentTimeMillis());
        }
    }

    static void put(UUID uuid, long time) {
        Vars.rtp.put(uuid, time);
        Update update = new Update(EterniaRTP.getString(ConfigStrings.TABLE_RTP));
        update.set.set("time", time);
        update.where.set("uuid", uuid.toString());
        SQL.executeAsync(update);
    }

}
