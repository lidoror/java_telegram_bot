package com.Oranim.TelegramBot.db;

import com.Oranim.TelegramBot.utils.FunctionsUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseListAction {


    IDatabase database = new MariaDB();

    public DatabaseListAction(){}

    /**
     *
     * @param month
     * @return
     * @throws SQLException
     */
    public List<ShoppingMgmtRecord> getMonthByExpense(String month) throws SQLException {
        return Optional.of(database.dbRecordToList().stream()
                .filter(date -> date.purchaseDate().split("-")[1].contains(month))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthByExpenses"));
    }

    public String getMonthlyExpenses() throws SQLException {
        return FunctionsUtils.listOutputFormatter(Optional.of(database.dbRecordToList().stream()
                .filter(date -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(date.purchaseDate()))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthlyExpenses")));
    }

    public List<ShoppingMgmtRecord> getMonthlyExpensesAsList() throws SQLException {
        return Optional.of(database.dbRecordToList().stream()
                .filter(date -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(date.purchaseDate()))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthlyExpenses"));
    }

    public String getTotalMonthSpending() throws SQLException {
        return String.valueOf(Optional.of(database.dbRecordToList().stream().filter(date -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(date.purchaseDate()))
                .map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum()).orElseThrow(()-> new SQLException("Exception in getTotalSpending")));
    }

    private List<ShoppingMgmtRecord> getMonthlyCategoryAsList(String category)throws SQLException{
        return Optional.of(database.dbRecordToList().stream()
                .filter(filter -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(filter.purchaseDate()) && filter.company().equals(category))
                .collect(Collectors.toList())).orElseThrow(()-> new SQLException("Exception in getMonthlyCategoryAsList"));
    }

    public List<ShoppingMgmtRecord> getMonthlyCategoryRecord(String category)throws SQLException{
        return getMonthlyCategoryAsList(category);
    }

    public String getCategoryMonthlySpent(String category) throws SQLException{
        return String.valueOf(getMonthlyCategoryAsList(category).stream().map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum());
    }

    public List<String> getCurrentMonthProducts()throws SQLException{
        return database.dbRecordToList().stream().filter(product -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(product.purchaseDate()))
                .map(ShoppingMgmtRecord::product).collect(Collectors.toList());
    }

    public List<String> getCurrentMonthPrices()throws SQLException{
        return database.dbRecordToList().stream().filter(price -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(price.purchaseDate()))
                .map(ShoppingMgmtRecord::price).collect(Collectors.toList());
    }


}
