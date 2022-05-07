package com.TelegramBot.db;

import com.TelegramBot.utils.FunctionsUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseFilter {

    IDatabase database = new MariaDB();

    public DatabaseFilter(){}


    public String getMonthByExpense(String month) throws SQLException {
        return FunctionsUtils.listOutputFormatter(Optional.of(database.DbRecordToList().stream()
                .filter(date -> date.purchaseDate().split("-")[1].contains(month))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthByExpenses")));
    }

    public String getMonthlyExpenses() throws SQLException {
        return FunctionsUtils.listOutputFormatter(Optional.of(database.DbRecordToList().stream()
                .filter(date -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(date.purchaseDate()))
                .collect(Collectors.toList())).orElseThrow(()->new SQLException("Exception in getMonthlyExpenses")));
    }

    public String getTotalMonthSpending() throws SQLException {
        return String.valueOf(Optional.of(database.DbRecordToList().stream().filter(date -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(date.purchaseDate()))
                .map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum()).orElseThrow(()-> new SQLException("Exception in getTotalSpending")));
    }

    private List<ShoppingMgmtRecord> getMonthlyCategoryAsList(String category)throws SQLException{
        return Optional.of(database.DbRecordToList().stream()
                .filter(filter -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(filter.purchaseDate()) && filter.company().equals(category))
                .collect(Collectors.toList())).orElseThrow(()-> new SQLException("Exception in getMonthlyCategoryAsList"));
    }

    public String getMonthlyCategoryRecord(String category)throws SQLException{
        return FunctionsUtils.listOutputFormatter(getMonthlyCategoryAsList(category));
    }

    public String getCategoryMonthlySpent(String category) throws SQLException{
        return String.valueOf(getMonthlyCategoryAsList(category).stream().map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum());
    }

    public List<String> getCurrentMonthProducts()throws SQLException{
        return database.DbRecordToList().stream().filter(product -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(product.purchaseDate()))
                .map(ShoppingMgmtRecord::product).collect(Collectors.toList());
    }

    public List<String> getCurrentMonthPrices()throws SQLException{
        return database.DbRecordToList().stream().filter(price -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(price.purchaseDate()))
                .map(ShoppingMgmtRecord::price).collect(Collectors.toList());
    }
    public List<ShoppingMgmtRecord> getCurrentMonthShoppingList()throws SQLException{
        return database.DbRecordToList().stream().filter(price -> FunctionsUtils.checkIfGivenMonthEqualToCurrentMonth(price.purchaseDate()))
                .collect(Collectors.toList());
    }

}
