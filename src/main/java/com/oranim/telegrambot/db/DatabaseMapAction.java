package com.oranim.telegrambot.db;

import java.sql.SQLException;

public class DatabaseMapAction {

    //   IDatabase database = new MariaDB();
    IDatabase database = new MariaDB();

    public String dbRecordsToMap(int mapKey) throws SQLException {
        return String.valueOf(database.dbListToMap().get(mapKey));
    }

    ;
}
