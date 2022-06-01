package com.TelegramBot.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDatabase {
    void updateDB(String product, String price, String company, String note);

    List<ShoppingMgmtRecord> dbRecordToList() throws SQLException;

    void setDbParameter(String command);

    boolean checkConnection() throws SQLException;

    Map<Integer,ShoppingMgmtRecord> dbListToMap()throws SQLException;
}
