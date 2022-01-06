package com.TelegramBot;

public class Balance {
    private double salary = 8900;
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
