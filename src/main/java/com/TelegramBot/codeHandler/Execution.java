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
                message.setText(operations.getStartMessage());
                keyboard.keyboard1(message);
            }

            case "expenses" ->{
                message.setText(operations.getExpenseMessage());
            }
            case "refund" ->{
                message.setText(operations.getRefundMessage());
            }


            case "balance"->
                    message.setText(balance.getStringBalance());

            case "monthly spent"-> {
                message.setText(db.getMonthlySpent());
                return;
            }
            case "monthly expenses"->{
                message.setText(db.getMonthlyExpenses());
                return;
            }


            case "overall expenses" -> {
                message.setText(db.readAll());
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

           case "r" -> message.setText(db.sumAllMoneySpent());



            default -> message.setText("We are very sorry, this function is not working yet.");
        }

        try {

            if(command.contains("SendChatId.admin-")){
                message.setText(command.split("-")[1]);
                return;
            }
            if (command.contains("checkDBS.admin")){
                Operations operation = new Operations();
                message.setText(operation.dbKeyboardCheck());
                return;
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

