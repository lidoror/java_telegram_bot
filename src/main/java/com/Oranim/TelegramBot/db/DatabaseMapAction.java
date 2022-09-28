package com.Oranim.TelegramBot.db;

import java.sql.SQLException;

public class DatabaseMapAction {

    //   IDatabase database = new MariaDB();
    IDatabase database = new Postgres();

    public String dbRecordsToMap(int mapKey) throws SQLException {
        return String.valueOf(database.dbListToMap().get(mapKey));
    }

    ;
}
