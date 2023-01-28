package com.oranim.telegrambot.db;

import com.oranim.telegrambot.utils.FunctionsUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseAction {


    IDatabase database = new MariaDB();

    public DatabaseAction(){}

    /**
     * this function return records between two dates
     * @return records from database between two dates
     * @throws SQLException
     */
    public List<ShoppingMgmtRecord> getExpensesByDates(String month , String year) throws SQLException {
        String startDate = "%s-%s-01".formatted(year,month);
        String endDate = "%s-%s-31".formatted(year,month);
        return Optional.of(new ArrayList<>(database.getItemsFromDbBetweenDates(startDate, endDate))).orElseThrow(() -> new SQLException("Exception in getMonthByExpenses"));
    }


    /**
     * this function calculate the sum of money spent between two dates
     * @param month the month we calc
     * @param year the year we calc
     * @return thew sum of money spent between two dates as a string
     * @throws SQLException
     */
    public String getTotalMoneySpentCurrentMonth(String month ,String year) throws SQLException {
        return String.valueOf(Optional.of(getExpensesByDates(month,year).stream().map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum()).orElseThrow(()-> new SQLException("Exception in getTotalSpending")));
    }


    private List<ShoppingMgmtRecord> getMonthlyCategoryAsList(String category)throws SQLException{
        return Optional.of(getExpensesByDates(FunctionsUtils.getCurrentMonth(),FunctionsUtils.getCurrentYear()).stream()
                .filter(filter -> filter.category().equals(category))
                .collect(Collectors.toList())).orElseThrow(()-> new SQLException("Exception in getMonthlyCategoryAsList"));
    }

    public List<ShoppingMgmtRecord> getMonthlyCategoryRecord(String category)throws SQLException{
        return getMonthlyCategoryAsList(category);
    }

    public String getCategoryMonthlySpent(String category) throws SQLException{
        return String.valueOf(getMonthlyCategoryAsList(category).stream().map(ShoppingMgmtRecord::price).mapToDouble(Double::parseDouble).sum());
    }

    public String dbRecordsToMap(int mapKey) throws SQLException {
        return String.valueOf(database.dbListToMap().get(mapKey));
    }




}
