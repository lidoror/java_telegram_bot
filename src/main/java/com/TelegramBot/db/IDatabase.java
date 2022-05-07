package com.TelegramBot.db;

import java.sql.SQLException;
import java.util.List;

public interface IDatabase {
    void updateDB(String product, String price, String company, String note);

    List<ShoppingMgmtRecord> DbRecordToList() throws SQLException;

    void setDbParameter(String command);

    boolean checkConnection() throws SQLException;

    String DbRecordsToMap(Integer mapKey) throws SQLException;
}
