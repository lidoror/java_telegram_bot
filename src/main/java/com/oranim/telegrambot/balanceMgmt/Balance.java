package com.oranim.telegrambot.balanceMgmt;

import com.oranim.telegrambot.db.DatabaseListAction;
import com.oranim.telegrambot.utils.BotLogging;
import com.oranim.telegrambot.utils.JsonWorkloads;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class Balance {

    private double balance;
    private final double firstSalary = getSalary("First_Salary");
    private final double secondSalary = getSalary("Second_Salary");
    private final double salary = firstSalary + secondSalary;

    public Double getBalance() throws SQLException {
        BotLogging.setInfoLog(classLog("getBalance","this method can throw sql exception"));
        balance = salary - Double.parseDouble(new DatabaseListAction().getTotalMonthSpending());
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

    private double getSalary(String salary) {
        JsonWorkloads jsonWorkloads = new JsonWorkloads();
        String salaryToReturn = jsonWorkloads.jsonReader("./vars.json").get(salary).toString();
        return Double.parseDouble(salaryToReturn);

    }


    private String classLog(String method, String description) {
        return "Exception occur in class: %s , method: %s Description: %s".formatted(Balance.class.getName(), method, description);
    }
}
