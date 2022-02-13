package com.TelegramBot.balanceMgmt;

import com.TelegramBot.db.MariaDB;

public class Balance {
    private final double salary = 17350;
    private static double balance;
    MariaDB db = new MariaDB();



    public Double getBalance(){
        return salary - Double.parseDouble(db.getMonthlySpent());
    }

    public void addToBalance(String num){
        balance += Double.parseDouble(num);

    }

    public String getSalary(){
        return String.valueOf(salary);
    }

    public String getStringBalance(){
        return String.valueOf(getBalance());
    }
}
