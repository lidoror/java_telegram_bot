package com.oranim.telegrambot.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDatabase {
    void insertDataToDB(String product, String price, String company, String note);

    List<ShoppingMgmtRecord> getAllRecordsFromDb() throws SQLException;

    void setDbParameter(String command);

    boolean checkConnection() throws SQLException;

    List<ShoppingMgmtRecord> getItemsFromDbBetweenDates(String startDate, String endDate) ;

    Map<Integer,ShoppingMgmtRecord> dbListToMap()throws SQLException;
}
