package br.com.eterniaserver.eterniartp.eternialib;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniartp.Constants;

public class Tables {

    public Tables() {
        if (EterniaLib.getMySQL()) {
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_TIME, "(uuid varchar(36), time bigint(20))"), false);
        } else {
            EQueries.executeQuery(Constants.getQueryCreateTable(Constants.TABLE_TIME, "(uuid varchar(36), time integer)"), false);
        }
    }

}
