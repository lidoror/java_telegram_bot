package com.oranim.telegrambot.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.oranim.telegrambot.models.ExpensesModel;

public interface IExpensesDao {
    void insertDataToDB(String product, String price, String company, String note);

    List<ExpensesModel> getAllRecordsFromDb() throws SQLException;


    boolean checkConnection() throws SQLException;

    List<ExpensesModel> getItemsFromDbBetweenDates(String startDate, String endDate) ;

    Map<Integer, ExpensesModel> dbListToMap()throws SQLException;
}
