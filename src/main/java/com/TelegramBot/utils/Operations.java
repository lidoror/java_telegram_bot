package com.TelegramBot.utils;

import com.TelegramBot.db.MariaDB;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.LocalDate;

public class Operations {
    MariaDB db = new MariaDB();

    public Operations(){}

    @NotNull
    public String getPrice(String price) {
        return price.split(" ")[1];
    }
    @NotNull
    public String getProduct(String product) {
        return product.split(" ")[0];
    }
    @NotNull
    public String getCompany(String company){
        return company.split(" ")[2];
    }

    public boolean isNumeric(String str){
        return NumberUtils.isNumber(str);
    }


    @NotNull
    public StringBuilder getNote(String note){
        String[] messageSplitter = note.split(" ");
        StringBuilder comment = new StringBuilder();
        int i;
        for ( i = 3; i <= messageSplitter.length-2; i++) {
            comment.append(messageSplitter[i]);
            comment.append(" ");
        }
        comment.append(messageSplitter[i]);
        return comment;
    }


    public String inputCheck(String str){
        return
                "product: " + getProduct(str) +
                        "\nprice: " + getPrice(str) +
                        "\ncompany: " + getCompany(str) +
                        "\nNote: "  + getNote(str);
    }


    public String dbKeyboardCheck() throws SQLException {
        MariaDB db = new MariaDB();
        if (db.checkConnection()){
            return "Active";
        }else
            throw new SQLException("Inactive");
    }


    public void setDbParameter(String command) {
        db.updateDB(getProduct(command),
                getPrice(command), getCompany(command),
                String.valueOf(getNote(command)));
    }


    public boolean checkForCurrentMonth(String givenDate){
        LocalDate currentDate = LocalDate.now();
        LocalDate monthToCheck = LocalDate.parse(givenDate);
        return currentDate.getMonth().equals(monthToCheck.getMonth());
    }

    public String getRefundMessage(){
        return "please enter the refund\n" +
                "enter the refund in this order \n" +
                "order->-price->company\n" +
                "dont forget the - sign before the price";
    }

    public String getExpenseMessage(){
        return "Please enter an expense" +
                "\nenter the expense in this order \nproduct->price->company";
    }

    public String getStartMessage(){
        return "Hi this is an expenses manager bot" +
                "\nplease choose an option:";
    }

    public String chooseOptionPrompt(){
        return "Choose an option: ";
    }




}
