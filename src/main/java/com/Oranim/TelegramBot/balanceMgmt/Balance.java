package com.Oranim.TelegramBot.balanceMgmt;

import com.Oranim.TelegramBot.Exception.IllegalSalaryException;
import com.Oranim.TelegramBot.db.DatabaseListAction;
import com.Oranim.TelegramBot.utils.BotLogging;
import java.sql.SQLException;
import java.text.DecimalFormat;


public class Balance {

    private double balance;
    private static double firstSalary;
    private static double secondSalary;
    private final double salary = firstSalary + secondSalary;

    DatabaseListAction databaseListAction = new DatabaseListAction();

    public Double getBalance() throws SQLException {
        balance = salary - Double.parseDouble(databaseListAction.getTotalMonthSpending());
        return balance;
    }

    public void addToBalance(String amountToAdd) {
        balance += Double.parseDouble(amountToAdd);

    }

    public String getStringBalance() throws SQLException {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(getBalance());
    }

    public void setFirstSalary(double firstSalary) throws IllegalSalaryException {
        if (System.getenv("FIRST_SALARY").isEmpty()) {
            boolean salaryLessThanZero = firstSalary < 0;

            if (salaryLessThanZero) {
                BotLogging.setInfoLog(classLog("Blance", "setFirstSalary"));
                throw new IllegalSalaryException("Salary cant be under 0");
            }

            Balance.firstSalary = firstSalary;
            return;

        }
        BotLogging.setInfoLog(classLog("Blance", "setFirstSalary", "First salary initialized via env"));
        Balance.firstSalary = Double.parseDouble(System.getenv("FIRST_SALARY"));

    }

    public void setSecondSalary(double secondSalary) throws IllegalSalaryException {

        if (System.getenv("SECOND_SALARY").isEmpty()) {
            boolean salaryLessThanZero = firstSalary < 0;

            if (salaryLessThanZero) {
                BotLogging.setInfoLog(classLog("Blance", "setSeconderySalary"));
                throw new IllegalSalaryException("Salary cant be under 0");
            }

            Balance.secondSalary = secondSalary;
            return;
        }
        BotLogging.setInfoLog(classLog("Blance", "setSecondSalary", "Second salary initialized via env"));
        Balance.secondSalary = Double.parseDouble(System.getenv("SECOND_SALARY"));

    }

    private String classLog(String className, String method) {
        return "Exception accure in class: %s , method: %s ".formatted(className, method);
    }

    private String classLog(String className, String method, String description) {
        return "Exception accure in class: %s , method: %s Description: %s".formatted(className, method, description);
    }
}
