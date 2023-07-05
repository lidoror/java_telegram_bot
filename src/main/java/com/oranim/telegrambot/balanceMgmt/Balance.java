package com.oranim.telegrambot.balanceMgmt;

import com.oranim.telegrambot.db.MessagesService;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.FunctionsUtils;
import com.oranim.telegrambot.FileHandelers.JsonWorkloads;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class Balance {

    private double balance;


    public Double getBalance() throws SQLException {
        BotLogging.setInfoLog(classLog("getBalance","this method can throw sql exception"));
        balance = new Salary().getSalarySum() - Double.parseDouble(new MessagesService().getTotalMoneySpentCurrentMonth(FunctionsUtils.getCurrentMonth(),FunctionsUtils.getCurrentMonth()));
        return balance;
    }

    public void addToBalance(String amountToAdd) {
        balance += Double.parseDouble(amountToAdd);
    }

    public String getStringBalance() throws SQLException {
        BotLogging.setInfoLog(classLog("getStringBalance","this method can throw sql exception"));
        //DecimalFormat.format limit the number of numbers after the dot
        return new DecimalFormat("0.00").format(getBalance());
    }




    private String classLog(String method, String description) {
        return "Exception occur in class: %s , method: %s Description: %s".formatted(Balance.class.getName(), method, description);
    }
}
