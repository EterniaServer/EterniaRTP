package br.com.eterniaserver.eterniartp.configurations.configs;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.CreateTable;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.enums.ConfigStrings;

public class TableCfg {

    public TableCfg() {
        CreateTable createTable;
        if (EterniaLib.getMySQL()) {
            createTable = new CreateTable(EterniaRTP.getString(ConfigStrings.TABLE_RTP));
            createTable.columns.set("id INT AUTO_INCREMENT NOT NULL PRIMARY KEY", "uuid VARCHAR(36)", "time BIGINT(20)");
            SQL.execute(createTable);
        } else {
            createTable = new CreateTable(EterniaRTP.getString(ConfigStrings.TABLE_RTP));
            createTable.columns.set("uuid VARCHAR(36)", "time INTEGER");
            SQL.execute(createTable);
        }
    }

}
