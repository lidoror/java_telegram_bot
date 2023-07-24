package com.oranim.telegrambot.managers;

import com.oranim.telegrambot.services.ExpensesService;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.LogWarningLevel;

import java.text.DecimalFormat;

public class BalanceManager {

    private double balance;
    private final SalaryManager salaryManager;
    private final ExpensesService expensesService;

    public BalanceManager() {
        salaryManager = new SalaryManager();
        expensesService = new ExpensesService();
    }

    public double getBalance() {
        BotLogging.createLog(LogWarningLevel.INFO, BalanceManager.class.getName(), "getBalance", "trying to get balance");

        balance = salaryManager.getSalarySum() - Double.parseDouble(
                expensesService.sumMoneySpentCurrentMonth());
        return balance;
    }

    public void addToBalance(String amountToAdd) {
        balance += Double.parseDouble(amountToAdd);
    }

    public String formatGetBalance() {
        BotLogging.createLog(LogWarningLevel.INFO,BalanceManager.class.getName(),"getStringBalance", "formatting the balance");
        //DecimalFormat.format limit the number of numbers after the dot
        return new DecimalFormat("0.00").format(getBalance());
    }

}
