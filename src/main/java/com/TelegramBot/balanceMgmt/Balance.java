package com.TelegramBot.balanceMgmt;

import com.TelegramBot.db.MariaDB;
import java.sql.SQLException;


public class Balance {
    private final double salary = 17350;
    private double balance;
    MariaDB db = new MariaDB();

    public Balance(){}

    public Double getBalance()throws SQLException {
        balance = salary - Double.parseDouble(db.getTotalMonthSpending());
        return balance;
    }

    public void addToBalance(String num){
        balance += Double.parseDouble(num);

    }


    public String getStringBalance()throws SQLException{
        return String.valueOf(getBalance());
    }
}
