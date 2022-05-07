package com.TelegramBot.balanceMgmt;

import com.TelegramBot.Exception.IllegalSalaryException;
import com.TelegramBot.db.DatabaseFilter;

import java.sql.SQLException;
import java.text.DecimalFormat;


public class Balance {

    private double balance;
    private static double firstSalary;
    private static double secondSalary;
    private final double salary = firstSalary + secondSalary;

    DatabaseFilter databaseFilter = new DatabaseFilter();

    public Double getBalance()throws SQLException {
        balance = salary - Double.parseDouble(databaseFilter.getTotalMonthSpending());
        return balance;
    }

    public void addToBalance(String amountToAdd){
        balance += Double.parseDouble(amountToAdd);

    }

    public String getStringBalance()throws SQLException{
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(getBalance());
    }

    public void setFirstSalary(double firstSalary) throws IllegalSalaryException {
        boolean salaryLessThanZero = firstSalary < 0;

        if (salaryLessThanZero){
            throw new IllegalSalaryException("Salary cant be under 0");
        } else
            Balance.firstSalary = firstSalary;
    }

    public void setSecondSalary(double secondSalary)throws IllegalSalaryException{
        boolean salaryLessThanZero = firstSalary < 0;

        if (salaryLessThanZero){
            throw new IllegalSalaryException("Salary cant be under 0");
        } else
            Balance.secondSalary = secondSalary;

    }
}
