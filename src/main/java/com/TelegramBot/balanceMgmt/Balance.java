package com.TelegramBot.balanceMgmt;

public class Balance {
    private final double salary = 8900.0;
    private static double balance;

    public String getSaving(){
        return String.valueOf(salary - balance);
    }

    public String getBalance(){
        return String.valueOf(balance);
    }

    public void addToBalance(String num){
        balance += Double.parseDouble(num);

    }

    public String getSalary(){
        return String.valueOf(salary);
    }
}
