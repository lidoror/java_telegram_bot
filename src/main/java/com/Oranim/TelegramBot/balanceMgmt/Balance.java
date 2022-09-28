package com.Oranim.TelegramBot.balanceMgmt;

import com.Oranim.TelegramBot.Exception.IllegalSalaryException;
import com.Oranim.TelegramBot.db.DatabaseListAction;

import java.sql.SQLException;
import java.text.DecimalFormat;


public class Balance {

    private double balance;
    private static double firstSalary;
    private static double secondSalary;
    private final double salary = firstSalary + secondSalary;

    DatabaseListAction databaseListAction = new DatabaseListAction();

    public Double getBalance()throws SQLException {
        balance = salary - Double.parseDouble(databaseListAction.getTotalMonthSpending());
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
