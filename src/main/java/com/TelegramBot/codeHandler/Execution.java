package com.TelegramBot.codeHandler;

import com.TelegramBot.balanceMgmt.Balance;
import com.TelegramBot.db.MariaDB;
import com.TelegramBot.keyboards.CustomKeyboard;
import com.TelegramBot.keyboards.InlineKeyboard;
import com.TelegramBot.utils.Company;
import com.TelegramBot.utils.Operations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.sql.SQLException;
import java.util.Locale;


public class Execution {
    CustomKeyboard keyboard = new CustomKeyboard();
    Balance balance = new Balance();
    Company company = new Company();
    InlineKeyboard inLine = new InlineKeyboard();
    Operations operations = new Operations();
    MariaDB db = new MariaDB();

     public Execution(){}
    
    public void messageHandler(String command, SendMessage message) {

        switch (command.toLowerCase(Locale.ROOT)) {
            case "start"->{
                message.setText("Hi this is an expenses manager bot" +
                        "\nplease choose an option:");
                keyboard.keyboard1(message);
            }

            case "expenses" ->{
                message.setText("Please enter an expense" +
                        "\nenter the expense in this order \nproduct->price->company");
            }
            case "refund" ->{
                message.setText("please enter the refund\n" +
                        "enter the refund in this order \n" +
                        "order->-price->company\n" +
                        "dont forget the - sign before the price");
            }
            case "b"-> message.setText(balance.getBalance());

            case "s" -> message.setText(balance.getSalary());


            case "balance"->
                    message.setText(balance.getSaving());

            case "overall expenses", "all expenses" , "all refunds"-> {
                message.setText("not supported yet");
                return;
            }
            case "key" ->{
                message.setText("choose your option:");
                inLine.monthKeyboard(message);
            }

            case "admin.center.control" -> {
               message.setText("choose an option: ");
               inLine.adminKeyboard(message);
           }

           case "month" -> {
                message.setText("Choose your month:");
                inLine.monthKeyboard(message);
           }
           case "day" ->{
                message.setText("Choose your day:");
                inLine.dayKeyboard(message);

           }
           case "t" -> message.setText(db.getProductPrice());


           case "r" -> message.setText(db.sumAllMoneySpend());


            default -> message.setText("We are very sorry, this function is not working yet.");
        }

        try {

            if(command.contains("SendChatId.admin-")){
                message.setText(command.split("-")[1]);
                return;
            }
            if (command.contains("checkDB.admin")){
                Operations operation = new Operations();
                message.setText(operation.dbKeyboardCheck());
            }

            if(command.contains(" ") && command.contains("-") && company.getList().contains(operations.getCompany(command))){
                if (operations.isNumeric(operations.getPrice(command))) {
                    balance.addToBalance(operations.getPrice(command));
                    operations.setDbParameter(command);
                    message.setText("Refunded from balance");
                }

            }else if (command.contains(" ") && company.getList().contains(operations.getCompany(command))) {
                if (operations.isNumeric(operations.getPrice(command))) {
                    balance.addToBalance(operations.getPrice(command));
                    operations.setDbParameter(command);
                    message.setText("Added to balance.");
                }
            }

        }catch (NumberFormatException e){
            message.setText("incorrect input");
            System.err.println("NumberFormatException");
        }catch(ArrayIndexOutOfBoundsException e){
            message.setText("Incorrect input");
            System.err.println("ArrayIndexOutOfBoundsException");
        }catch (SQLException sqlException){
            System.err.println("No Connection to DB");
            message.setText("No Connection to DB");
        } catch(Exception e){
            System.err.println("exception");

        }

    }





}

