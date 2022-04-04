package com.TelegramBot.balanceMgmt;

import com.TelegramBot.db.MariaDB;
import java.sql.SQLException;


public class Balance {
    private final double salary = 17350;
    private static double balance;

    MariaDB db = new MariaDB();

    public Double getBalance()throws SQLException {
        return salary - Double.parseDouble(db.getTotalMonthSpending());
    }

    public void addToBalance(String num){
        balance += Double.parseDouble(num);

    }

    public String getSalary(){
        return String.valueOf(salary);
    }

    public String getStringBalance()throws SQLException{
        return String.valueOf(getBalance());
    }
}
